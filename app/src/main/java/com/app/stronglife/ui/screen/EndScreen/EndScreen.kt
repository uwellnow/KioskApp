package com.app.stronglife.ui.screen.EndScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.app.stronglife.R
import com.app.stronglife.data.remote.KioskLogger
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.ui.component.ErrorBox
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.cardPayGray
import com.app.stronglife.ui.theme.midGray
import com.app.stronglife.viewmodel.Gs805ViewModel
import com.app.stronglife.viewmodel.Gs805ViewModel.MachineEvent
import com.app.stronglife.viewmodel.ProductViewModel
import com.app.stronglife.viewmodel.ProductViewModelFactory
import com.app.stronglife.viewmodel.UserCodeViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.runBlocking

@Composable
fun EndScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    vm: Gs805ViewModel = viewModel(),
    apiKey: String
) {
    val scope = rememberCoroutineScope()
    val cartItems = cartViewModel.cartItems.value
    val products = productViewModel.products
    val userCodeViewModel = UserCodeViewModel.getInstance(RetrofitClient.api)


    LaunchedEffect(Unit) {
        if (products.isEmpty()) {
            productViewModel.fetchProducts()
        }
    }


    // KioskLogger 인스턴스 생성 (1번 코드 방식)
    val kioskLogger = remember {
        KioskLogger(
            apiKey = apiKey,
            service = RetrofitClient.api,
            externalScope = scope,
            machineId = apiKey.toLong(),
            storeName = "스트롱라이프 GFC점"
        )
    }

    // 1) 완료 신호(1회성), 2) 컵 치움 신호(정적 구간)
    val completedCh = remember { kotlinx.coroutines.channels.Channel<Unit>(capacity = 1) }
    val cupClearedCh = remember { kotlinx.coroutines.channels.Channel<Unit>(capacity = 1) }


    // 상태
    var totalJobs by remember { mutableStateOf(0) }
    var currentIndex by remember { mutableStateOf(0) }
    var inProgress by remember { mutableStateOf(false) }
    var lastError by remember { mutableStateOf<String?>(null) }
    val runOnce = remember { mutableStateOf(false) }

    // 플래그
    var awaitingCompletion by remember { mutableStateOf(false) }
    var awaitingCupClear by remember { mutableStateOf(false) }

    val isCurrentlyMaking = inProgress && totalJobs > 0

    Finish(
        currentDrinkIndex = currentIndex,
        totalDrinkCount = totalJobs,
        isInProgress = isCurrentlyMaking,
        errorMessage = lastError,
        onErrorConfirm = {
            navController.navigate("hello")
        }
    )

    // 정적 구간 길이/타임아웃
    val QUIET_MS = 1500L           // DrinkCompleted가 이 시간 이상 안 오면 컵 수거로 간주
    val MAKE_TIMEOUT_MS = 120_000L // 제조 완료 대기
    val CUP_TIMEOUT_MS  = 180_000L // 컵 수거 대기

    // 드레인 함수
    suspend fun drainChannels() {
        while (!completedCh.isEmpty) completedCh.tryReceive().getOrNull() ?: break
        while (!cupClearedCh.isEmpty) cupClearedCh.tryReceive().getOrNull() ?: break
    }

    // 이벤트 수신: DrinkCompleted 스팸 억제 + 정적 구간 타이머
    LaunchedEffect(Unit) {
        var quietJob: kotlinx.coroutines.Job? = null

        fun armQuietTimer() {
            quietJob?.cancel()
            // kioskLogger.logEvent("QuietTimer ARM (${QUIET_MS}ms)", false /* high=true 권장 */)
            // DrinkCompleted가 막 왔으니, QUIET_MS 동안 추가 신호가 없으면 컵 수거로 본다
            quietJob = launch {
                delay(QUIET_MS)
                if (awaitingCupClear) {
                    kioskLogger.logEvent("CupCleared (quiet ${QUIET_MS}ms)", false)
                    cupClearedCh.trySend(Unit)
                }
            }
        }

        vm.events.collectLatest { ev ->
            when (ev) {
                is Gs805ViewModel.MachineEvent.RawDataReceived -> {
                    kioskLogger.logEvent("RawDataReceived", false, responseHex = ev.hex)
                }
                is Gs805ViewModel.MachineEvent.DrinkCompleted -> {
                    // 1) 제조 완료 인정 단계
                    if (awaitingCompletion) {
                        val sent = completedCh.trySend(Unit).isSuccess
                        if (sent) {
                            kioskLogger.logEvent("DrinkCompleted (accepted)", false, responseHex = ev.hex)
                            // DrinkCompleted 발생 시 products를 제외한 모든 정보 초기화

                        } else {
                            kioskLogger.logEvent("DrinkCompleted (ignored: buffered)", false, responseHex = ev.hex)
                        }
                    } else {
                        kioskLogger.logEvent("DrinkCompleted (ignored: not awaiting)", false, responseHex = ev.hex)
                    }

                    // 2) 컵 수거 감지 단계: 반복 송출을 이용해 ‘정적 구간’ 판단
                    if (awaitingCupClear) {
                        // 신호가 또 왔으니 아직 컵이 안 치워진 것 → 타이머를 리셋
                        armQuietTimer()
                        kioskLogger.logEvent("DrinkCompleted (keep-alive, waiting quiet)", false)
                    }
                }
                is Gs805ViewModel.MachineEvent.CupDropped -> {
                    kioskLogger.logEvent("CupDropped", false, responseHex = ev.hex)
                }
                is Gs805ViewModel.MachineEvent.Offline -> {
                    kioskLogger.logEvent("Offline cmd=0x${ev.cmd.toString(16)}", true)
                }
                is Gs805ViewModel.MachineEvent.ErrorCode -> {
                    val msg = if (ev.code == -1) "SerialCommunicationError"
                    else "MachineErrorCode=0x${ev.code.toString(16)}"
                    kioskLogger.logEvent(msg, true)
                }
            }
        }
    }

    // 제조 오케스트레이션
    LaunchedEffect(cartItems, products) {
        if (runOnce.value) return@LaunchedEffect
        runOnce.value = true
        kioskLogger.logEvent("Cart=${cartItems.size}, Products=${products.size}", false)

        val queue = buildList {
            cartItems.forEach { ci ->
                val p = products.find { it.id == ci.product.id }
                if (p == null) {
                    kioskLogger.logEvent("Product not found: id=${ci.product.id}", true)
                } else {
                    repeat(ci.quantity) { add(p) }
                }
            }
        }

        totalJobs = queue.size
        currentIndex = 0
        lastError = null

        if (queue.isEmpty()) {
            kioskLogger.logEvent("Queue empty -> home", false)
            runCatching { withTimeout(1000) { kioskLogger.flush() } } // 1s 한도 flush
            navController.navigate("hello"); return@LaunchedEffect
        }

        inProgress = true

        val serialOk = vm.startSerial()
        kioskLogger.logEvent("SerialStart", !serialOk, responseHex = if (serialOk) "OK" else null)
        if (!serialOk) { lastError = "시리얼 연결 실패"; inProgress = false; return@LaunchedEffect }

        delay(500)

        val queryResult = vm.queryErrorCode()
        kioskLogger.logEvent(
            "QueryErrorCode",
            (queryResult.responseHex == null || queryResult.businessResult == -1),
            commandHex = queryResult.sentHex,
            responseHex = queryResult.responseHex
        )

        outer@ for ((idx, product) in queue.withIndex()) {
            currentIndex = idx + 1

            // 이전 잔 잔여 신호 제거
            awaitingCompletion = false
            awaitingCupClear = false
            drainChannels()

            // 레시피 저장
            val slotsPairs = product.recipeSlots.map { (it.getOrNull(0) ?: 0) to (it.getOrNull(1) ?: 0) }
            kioskLogger.logEvent("RecipeSlots count: ${slotsPairs.size}, data: $slotsPairs", false)
            val recipeOk = vm.saveRecipe3(0x11, slotsPairs)
            kioskLogger.logEvent(
                "SaveRecipe ${product.name} ($currentIndex/$totalJobs)",
                !recipeOk.businessResult,
                commandHex = recipeOk.sentHex,
                responseHex = recipeOk.responseHex
            )
            if (!recipeOk.businessResult) { lastError = "레시피 저장 실패: ${product.name}"; break@outer }

            delay(500)

            // 제조 시작
            val make = vm.makeDrinkNow(0x11, localOrCmd = 0x02)
            kioskLogger.logEvent(
                "MakeDrink ${product.name} ($currentIndex/$totalJobs)",
                !make.businessResult,
                commandHex = make.sentHex,
                responseHex = make.responseHex
            )
            if (!make.businessResult) { lastError = "제조 시작 실패: ${product.name}"; break@outer }

            // 1) 제조 완료(첫 DrinkCompleted)까지 대기
            awaitingCompletion = true
            try {
                withTimeout(MAKE_TIMEOUT_MS) { completedCh.receive() }
                kioskLogger.logEvent("Completed ${product.name} ($currentIndex/$totalJobs)", false)
            } catch (t: TimeoutCancellationException) {
                lastError = "제조 완료 타임아웃: ${product.name}"
                kioskLogger.logEvent(lastError!!, true)
                break@outer
            } finally {
                awaitingCompletion = false
                // 혹시 남아있으면 제거
                while (!completedCh.isEmpty) completedCh.tryReceive().getOrNull() ?: break
            }

            // 2) 컵 수거(DrinkCompleted가 QUIET_MS 동안 더 이상 안 옴)까지 대기
            awaitingCupClear = true
            try {
                withTimeout(CUP_TIMEOUT_MS) { cupClearedCh.receive() }
                kioskLogger.logEvent("Cup taken for ${product.name}", false)
            } catch (t: TimeoutCancellationException) {
                lastError = "컵 수거 타임아웃: ${product.name}"
                kioskLogger.logEvent(lastError!!, true)
                break@outer
            } finally {
                awaitingCupClear = false
                // 안전 드레인
                while (!cupClearedCh.isEmpty) cupClearedCh.tryReceive().getOrNull() ?: break
            }

            delay(500)
            if (idx < queue.lastIndex) {
                kioskLogger.logEvent("Pause before next drink = 20s", false)
                delay(20_000L)
            }
        }

        inProgress = false

        if (currentIndex == totalJobs && lastError == null) {
            kioskLogger.logEvent("All jobs completed -> home", false)
            runCatching { withTimeout(1000) { kioskLogger.flush() } } // 1s 한도 flush
            navController.navigate("hello")

        } else {
            kioskLogger.logEvent("Jobs ended with error: $lastError", true)
            // 필요시 오류 화면/재시도
        }
    }


    DisposableEffect(Unit) {
        onDispose {
            // 로그 유실 줄이기
            vm.stopSerial()
            runBlocking {
                runCatching { withTimeout(1500) { kioskLogger.flush(1200) } }
                runCatching { withTimeout(2000) { kioskLogger.closeAndJoin(1800) } }
            }
            
        }
    }

}
