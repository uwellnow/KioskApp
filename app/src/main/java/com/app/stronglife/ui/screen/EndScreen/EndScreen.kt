package com.app.stronglife.ui.screen.EndScreen

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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.app.stronglife.R
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.cardPayGray
import com.app.stronglife.ui.theme.midGray
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.stronglife.data.model.KioskLogPayload
import com.app.stronglife.data.remote.ApiService
import com.app.stronglife.data.remote.KioskLogger
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.data.remote.RetrofitClient.api
import com.app.stronglife.util.nowIso
import com.app.stronglife.viewmodel.Gs805ViewModel
import com.app.stronglife.viewmodel.Gs805ViewModel.MachineEvent

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

    val api = remember { RetrofitClient.api }
    val scope = rememberCoroutineScope()

    val kioskLogger = remember {
        KioskLogger(
            apiKey = apiKey,
            service = RetrofitClient.api,
            externalScope = scope,
            machineId = 234,           // 실제 단말 ID
            storeName = "매장명"         // 필요 시
        )
    }


    LaunchedEffect(Unit) {
        val now = nowIso()

        // 1. 시리얼 연결 로그
        val serialOk = vm.startSerial()
        kioskLogger.logFrame(
            responseHex = "응답 HEX",
            commandHex = "보낸 HEX"
        )

        // 2. 상태 확인 로그
        val err = vm.queryErrorCode()
        if (err == 0) {
            kioskLogger.logFrame("응답 HEX", "AA55020C0D")
        } else {
            kioskLogger.logError(Throwable("ErrorCode=$err"), "AA55020C0D")
        }

        // 3. 레시피 저장 로그
        val slots = listOf(80 to 80, 0 to 0, 0 to 0, 0 to 0, 0 to 0, 0 to 0, 0 to 0, 0 to 0)
        val okRecipe = vm.saveRecipe3(0x11, slots)
        if (okRecipe) {
            kioskLogger.logFrame(
                responseHex = "응답 HEX",
                commandHex = "보낸 HEX"
            )
        } else {
            kioskLogger.logError(
                error = Throwable("SaveRecipe 실패"),
                commandHex = "보낸 HEX"
            )
        }

        // 4. 제조 시작 로그
        val okMake = vm.makeDrinkNow(0x11, localOrCmd = 0x02)
        if (okMake) {
            kioskLogger.logFrame(
                responseHex = "응답 HEX",
                commandHex = "보낸 HEX"
            )
        } else {
            kioskLogger.logError(
                error = Throwable("MakeDrink 실패"),
                commandHex = "보낸 HEX"
            )
        }
    }


    // 1) 장치 이벤트 수신 → 제조 완료 시 화면 전환 (기존 그대로)
    LaunchedEffect(Unit) {
        vm.events.collectLatest { ev ->
            when (ev) {
                is Gs805ViewModel.MachineEvent.DrinkCompleted -> {
                    kioskLogger.logFrame("응답 HEX", "보낸 HEX")
                    delay(300)
                    navController.navigate("first")
                }
                is Gs805ViewModel.MachineEvent.CupDropped -> {
                    kioskLogger.logFrame("응답 HEX", "보낸 HEX")
                }
                is Gs805ViewModel.MachineEvent.Offline-> {
                    kioskLogger.logError(Throwable("Offline cmd=0x${ev.cmd.toString(16)}"), "보낸 HEX")
                }
                is Gs805ViewModel.MachineEvent.ErrorCode -> {
                    kioskLogger.logError(Throwable("ErrorCode=${ev.code}"), "보낸 HEX")
                }
            }
        }
    }

    // 2) 시작 + 테스트 시퀀스(3계열) = 한 코루틴에서 순차 실행
    LaunchedEffect(Unit) {
        val ok = vm.startSerial()
        if (!ok) {
            println("Serial not available; running in mock-ish mode")
            // 여기선 장치가 없으니 실제 전송은 건너뜀(앱은 계속 UI 유지)
            return@LaunchedEffect
        }

        runCatching {
            // (a) 상태 확인 (0x0C)
            val err = vm.queryErrorCode()
            println("queryErrorCode() = 0x${err.toString(16)}")

            // (b) 레시피 저장 (0x15, 3계열) — 예시값
            val drinkNo = 0x11
            val slots = listOf(
                80 to 80, // 채널1
                0 to 0, 0 to 0, 0 to 0,
                0 to 0, 0 to 0, 0 to 0, 0 to 0
            )
            val okRecipe = vm.saveRecipe3(drinkNo, slots)
            println("saveRecipe3() = $okRecipe")
            check(okRecipe) { "레시피 저장 실패" }

            // (c) 즉시 제조 (0x01, LocalOrCmd=0x02)
            val okMake = vm.makeDrinkNow(drinkNo, localOrCmd = 0x02)
            println("makeDrinkNow() = $okMake")
            check(okMake) { "제조 시작 실패" }
            // 이후 완료 신호는 vm.events에서 수신
        }.onFailure { e ->
            e.printStackTrace() // onFailure 유지
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
                modifier = Modifier.width(imageWidDp).height(imageHeiDp)
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

private suspend fun sendLog(
    api: ApiService,
    apiKey: String,
    payload: KioskLogPayload
) {
    runCatching {
        val resp = api.postKioskLog(apiKey, payload)
        if (resp.isSuccessful) {
            println("로그 전송 성공: ${payload.errorId}")
        } else {
            println("로그 전송 실패: ${resp.code()} ${resp.message()}")
        }
    }.onFailure {
        it.printStackTrace()
    }
}

