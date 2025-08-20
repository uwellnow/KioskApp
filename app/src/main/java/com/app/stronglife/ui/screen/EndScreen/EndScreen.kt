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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

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


    Finish(navController, cartViewModel)

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

        // --- 제조 완료 이벤트를 기다리기 위한 채널 ---
    val drinkCompletedCh = remember { kotlinx.coroutines.channels.Channel<Unit>(capacity = kotlinx.coroutines.channels.Channel.BUFFERED) }

    // --- 진행상태 ---
    var totalJobs by remember { mutableStateOf(0) }
    var currentIndex by remember { mutableStateOf(0) }
    var inProgress by remember { mutableStateOf(false) }
    var lastError by remember { mutableStateOf<String?>(null) }


    // ----- 비동기 이벤트 수신부 -----
    LaunchedEffect(Unit) {
        vm.events.collectLatest { ev ->
            when (ev) {
                is Gs805ViewModel.MachineEvent.RawDataReceived -> {
                    kioskLogger.logEvent(detail = "RawDataReceived", isError = false, responseHex = ev.hex)
                }
                is Gs805ViewModel.MachineEvent.DrinkCompleted -> {
                    kioskLogger.logEvent(detail = "Event: DrinkCompleted", isError = false, responseHex = ev.hex)
                    // 제조 루프에서 대기 중인 receive()를 깨워줌
                    drinkCompletedCh.trySend(Unit)
                }
                is Gs805ViewModel.MachineEvent.CupDropped -> {
                    kioskLogger.logEvent(detail = "Event: CupDropped", isError = false, responseHex = ev.hex)
                }
                is Gs805ViewModel.MachineEvent.Offline -> {
                    kioskLogger.logEvent(detail = "Offline cmd=0x${ev.cmd.toString(16)}", isError = true)
                }
                is Gs805ViewModel.MachineEvent.ErrorCode -> {
                    val msg = if (ev.code == -1) "SerialCommunicationError"
                              else "MachineErrorCode=0x${ev.code.toString(16)}"
                    kioskLogger.logEvent(detail = msg, isError = true)
                }
            }
        }
    }


    // ----- 제조 오케스트레이션 -----
    LaunchedEffect(cartItems, products) {
        // 카트/상품 로깅
        kioskLogger.logEvent(
            detail = "Cart count=${cartItems.size}, Products count=${products.size}",
            isError = false
        )

        // 작업 큐(flatten): (product, 1) * quantity
        val queue = buildList {
            cartItems.forEach { ci ->
                val p = products.find { it.id == ci.product.id }
                if (p == null) {
                    kioskLogger.logEvent(
                        detail = "Product not found in products list: id=${ci.product.id}",
                        isError = true
                    )
                } else {
                    repeat(ci.quantity) {
                        add(p)
                    }
                }
            }
        }

        totalJobs = queue.size
        currentIndex = 0
        lastError = null

        if (queue.isEmpty()) {
            kioskLogger.logEvent(detail = "Queue is empty; navigating back", isError = false)
            navController.navigate("hello")
            return@LaunchedEffect
        }

        // 시리얼 연결 1회
        inProgress = true
        val serialOk = vm.startSerial()
        kioskLogger.logEvent(
            detail = "SerialStart",
            isError = !serialOk,
            responseHex = if (serialOk) "Connection successful" else null
        )
        if (!serialOk) {
            lastError = "시리얼 연결 실패"
            inProgress = false
            return@LaunchedEffect
        }

        delay(120L)

        // 상태 확인 1회
        val queryResult = vm.queryErrorCode()
        kioskLogger.logEvent(
            detail = "QueryErrorCode",
            isError = (queryResult.responseHex == null || queryResult.businessResult == -1),
            commandHex = queryResult.sentHex,
            responseHex = queryResult.responseHex
        )

        // 개별 제조 루프
        outer@ for ((idx, product) in queue.withIndex()) {
            currentIndex = idx + 1

            // 레시피 재구성: 서버에서 오는 recipeSlots: List<List<Int>> 형태라고 가정
            val slotsPairs: List<Pair<Int, Int>> =
                product.recipeSlots.map { (it.getOrNull(0) ?: 0) to (it.getOrNull(1) ?: 0) }

            // 3. 레시피 저장
            val recipeResult = vm.saveRecipe3(0x11, slotsPairs)
            kioskLogger.logEvent(
                detail = "SaveRecipe for ${product.name} (job $currentIndex/$totalJobs)",
                isError = !recipeResult.businessResult,
                commandHex = recipeResult.sentHex,
                responseHex = recipeResult.responseHex
            )
            if (!recipeResult.businessResult) {
                lastError = "레시피 저장 실패: ${product.name}"
                // 실패 시: 다음 항목 시도할지 중단할지 정책 선택
                // 여기서는 '중단' 선택
                break@outer
            }

            delay(120L)

            // 4. 제조 시작
            val makeResult = vm.makeDrinkNow(0x11, localOrCmd = 0x02)
            kioskLogger.logEvent(
                detail = "MakeDrink for ${product.name} (job $currentIndex/$totalJobs)",
                isError = !makeResult.businessResult,
                commandHex = makeResult.sentHex,
                responseHex = makeResult.responseHex
            )
            if (!makeResult.businessResult) {
                lastError = "제조 시작 실패: ${product.name}"
                break@outer
            }

            // 5. 이 음료가 완료될 때까지 DrinkCompleted 대기 (타임아웃 포함)
            try {
                withTimeout(120_000L) { // 필요 시 조정
                    drinkCompletedCh.receive()
                }
                kioskLogger.logEvent(
                    detail = "Completed ${product.name} (job $currentIndex/$totalJobs)",
                    isError = false
                )
            } catch (t: TimeoutCancellationException) {
                lastError = "제조 완료 타임아웃: ${product.name}"
                kioskLogger.logEvent(detail = lastError!!, isError = true)
                break@outer
            }

            // 다음 항목으로 진행
            delay(150L)
        }

        inProgress = false

        // 모두 완료 & 에러 없음 -> 초기 화면
        if (currentIndex == totalJobs && lastError == null) {
            kioskLogger.logEvent(detail = "All jobs completed -> navigate home", isError = false)
            navController.navigate("hello")
        } else {
            // 에러 발생 시: 여기서 에러 화면으로 보내거나 재시도 UI 노출 등
            kioskLogger.logEvent(detail = "Jobs ended with error: $lastError", isError = true)
            // 선택 1) 그대로 유지
            // 선택 2) navController.navigate("error") 등
        }
    }
}// private suspend fun sendLog(...)  <- 이 함수는 삭제합니다.

