package com.app.stronglife.ui.screen

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
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.cardPayGray
import com.app.stronglife.ui.theme.midGray
import com.app.stronglife.viewmodel.Gs805ViewModel
import com.app.stronglife.viewmodel.Gs805ViewModel.MachineEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EndScreen(
    navController: NavController,
    vm: Gs805ViewModel = viewModel(),
    apiKey: String
) {
    val density = LocalDensity.current
    val widDp = with(density) { 1231f.toDp() }
    val heightDp = with(density) { 824f.toDp() }
    val roundDp = with(density) { 32f.toDp() }
    val textSp = with(density) { 36f.toSp() }
    val space1Dp = with(density) { 81f.toDp() }
    val space2Dp = with(density) { 107f.toDp() }
    val imageWidDp = with(density) { 381f.toDp() }
    val imageHeiDp = with(density) { 68f.toDp() }

    // KioskLogger 인스턴스 생성 (1번 코드 방식)
    val scope = rememberCoroutineScope()
    val kioskLogger = remember {
        KioskLogger(
            apiKey = apiKey,
            service = RetrofitClient.api,
            externalScope = scope,
            machineId = 12345L,      // 실제 단말 ID로 교체 필요
            storeName = "스트롱라이프 GFC점" // 실제 매장명으로 교체 필요
        )
    }

    // 요청-응답 시퀀스 실행 및 로깅
    LaunchedEffect(Unit) {
        // 1. 시리얼 연결
        val serialOk = vm.startSerial()
        if (serialOk) {
            kioskLogger.logFrame(responseHex = "Serial connection successful", commandHex = null)
        } else {
            kioskLogger.logError(error = Throwable("SerialStart failed"), commandHex = null)
            return@LaunchedEffect // 실패 시 중단
        }

        // 2. 상태 확인
        val queryResult = vm.queryErrorCode()
        if (queryResult.responseHex != null && queryResult.businessResult != -1) {
            kioskLogger.logFrame(
                responseHex = queryResult.responseHex,
                commandHex = queryResult.sentHex
            )
        } else {
            kioskLogger.logError(
                error = Throwable("QueryErrorCode failed (code=${queryResult.businessResult})"),
                commandHex = queryResult.sentHex
            )
        }

        // 3. 레시피 저장
        val slots = listOf(80 to 80, 0 to 0, 0 to 0, 0 to 0, 0 to 0, 0 to 0, 0 to 0, 0 to 0)
        val recipeResult = vm.saveRecipe3(0x01, slots)
        if (recipeResult.businessResult) {
            kioskLogger.logFrame(
                responseHex = recipeResult.responseHex!!, // 성공 시 null이 아님을 보장
                commandHex = recipeResult.sentHex
            )
        } else {
            kioskLogger.logError(
                error = Throwable("SaveRecipe failed"),
                commandHex = recipeResult.sentHex
            )
        }

        // 4. 제조 시작
        val makeResult = vm.makeDrinkNow(0x01, localOrCmd = 0x02)
        if (makeResult.businessResult) {
            kioskLogger.logFrame(
                responseHex = makeResult.responseHex!!, // 성공 시 null이 아님을 보장
                commandHex = makeResult.sentHex
            )
        } else {
            kioskLogger.logError(
                error = Throwable("MakeDrink failed"),
                commandHex = makeResult.sentHex
            )
        }
    }

    // 비동기 이벤트 수신 및 로깅
    LaunchedEffect(Unit) {
        vm.events.collectLatest { ev ->
            when (ev) {
                is MachineEvent.DrinkCompleted -> {
                    kioskLogger.logFrame(responseHex = "Event: DrinkCompleted", commandHex = null)
                    delay(300)
                    navController.navigate("first")
                }
                is MachineEvent.CupDropped -> {
                    kioskLogger.logFrame(responseHex = "Event: CupDropped", commandHex = null)
                }
                is MachineEvent.Offline -> {
                    val message = "Offline cmd=0x${ev.cmd.toString(16)}"
                    kioskLogger.logError(error = Throwable(message), commandHex = null)
                }
                is MachineEvent.ErrorCode -> {
                    val message = if (ev.code == -1) {
                        "SerialCommunicationError"
                    } else {
                        "MachineErrorCode=0x${ev.code.toString(16)}"
                    }
                    kioskLogger.logError(error = Throwable(message), commandHex = null)
                }
            }
        }
    }

    // ---------------- 기존 UI 그대로 ----------------
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopBar(5, listOf("섭취시점 선택", "메뉴선택", "주문 확인", "결제하기", "결제완료"), navController)

        Spacer(modifier = Modifier.height(space1Dp))
        Column(
            modifier = Modifier
                .width(widDp)
                .height(heightDp)
                .background(color = Color.White, shape = RoundedCornerShape(roundDp))
                .border(2.dp, cardPayGray, RoundedCornerShape(roundDp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = R.drawable.uwellnow,
                contentDescription = "유웰나우 로고",
                modifier = Modifier
                    .width(imageWidDp)
                    .height(imageHeiDp)
            )

            Spacer(modifier = Modifier.height(space2Dp))

            Text(
                text = "결제가 완료되었어요!\n음료 제조가 시작되었습니다 잠시만 기다려주세요",
                style = TextStyle(
                    fontSize = textSp,
                    fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                    fontWeight = FontWeight.Bold,
                    color = midGray,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

// private suspend fun sendLog(...)  <- 이 함수는 삭제합니다.