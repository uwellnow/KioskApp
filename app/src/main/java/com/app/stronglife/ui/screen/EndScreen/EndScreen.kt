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
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.util.nowIso
import com.app.stronglife.viewmodel.Gs805ViewModel
import com.app.stronglife.viewmodel.Gs805ViewModel.MachineEvent
import java.util.concurrent.atomic.AtomicInteger
import kotlin.hashCode


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

    // 단말 식별 해시(16비트) + 시간(36비트) + 카운터(12비트) = 64비트 고유 ID
    val deviceHash16 = (apiKey.hashCode() and 0xFFFF).toLong()
    val counter = AtomicInteger(0)
    fun newUniqueId(): Long {
        val t36 = System.currentTimeMillis() and ((1L shl 36) - 1)
        val c12 = (counter.getAndIncrement() and 0xFFF).toLong()
        return (deviceHash16 shl 48) or (t36 shl 12) or c12
    }

    // [변경] ViewModel 함수 호출 및 로깅 로직 수정
    LaunchedEffect(Unit) {
//        val now = nowIso()

        // 1. 시리얼 연결 로그 (기존과 동일)
        val serialOk = vm.startSerial()
        sendLog(api, apiKey, KioskLogPayload(
            errorId = newUniqueId(),
            timestamp = nowIso(),
            errorType = if (serialOk) "FRAME" else "ERROR",
            errorDetail = "SerialStart"
        ))
        if (!serialOk) return@LaunchedEffect // 시리얼 실패 시 중단

        // 2. 상태 확인 로그
        val queryResult = vm.queryErrorCode()
        sendLog(api, apiKey, KioskLogPayload(
            errorId = newUniqueId(),
            timestamp = nowIso(),
            // 응답이 없거나(null) 비즈니스 결과가 실패(-1)면 에러로 간주
            errorType = if (queryResult.responseHex != null && queryResult.businessResult != -1) "FRAME" else "ERROR",
            errorDetail = "QueryErrorCode",
            commandSent = queryResult.sentHex,
            response = queryResult.responseHex ?: "NO_RESPONSE" // 응답 없으면 "NO_RESPONSE"
        ))

        // 3. 레시피 저장 로그
        val slots = listOf(80 to 80, 0 to 0, 0 to 0, 0 to 0, 0 to 0, 0 to 0, 0 to 0, 0 to 0)
        val recipeResult = vm.saveRecipe3(0x01, slots)
        sendLog(api, apiKey, KioskLogPayload(
            errorId = newUniqueId(),
            timestamp = nowIso(),
            errorType = if (recipeResult.businessResult) "FRAME" else "ERROR",
            errorDetail = "SaveRecipe",
            commandSent = recipeResult.sentHex,
            response = recipeResult.responseHex ?: "NO_RESPONSE"
        ))

        // 4. 제조 시작 로그
        val makeResult = vm.makeDrinkNow(0x01, localOrCmd = 0x02)
        sendLog(api, apiKey, KioskLogPayload(
            errorId = newUniqueId(),
            timestamp = nowIso(),
            errorType = if (makeResult.businessResult) "FRAME" else "ERROR",
            errorDetail = "MakeDrink",
            commandSent = makeResult.sentHex,
            response = makeResult.responseHex ?: "NO_RESPONSE"
        ))
    }


    // 1) 장치 이벤트 수신 → 제조 완료 시 화면 전환 (기존 그대로)
    LaunchedEffect(Unit) {
        vm.events.collectLatest { ev ->
            when (ev) {
                is Gs805ViewModel.MachineEvent.DrinkCompleted -> {
                    sendLog(api, apiKey, KioskLogPayload(
                        errorId = newUniqueId(),
                        timestamp = nowIso(),
                        errorType = "FRAME",
                        errorDetail = "DrinkCompleted"
                    ))
                    delay(300)
                    navController.navigate("first")
                }
                is Gs805ViewModel.MachineEvent.CupDropped -> {
                    sendLog(api, apiKey, KioskLogPayload(
                        errorId = newUniqueId(),
                        timestamp = nowIso(),
                        errorType = "FRAME",
                        errorDetail = "CupDropped"
                    ))
                }
                is Gs805ViewModel.MachineEvent.Offline-> {
                    sendLog(api, apiKey, KioskLogPayload(
                        errorId = newUniqueId(),
                        timestamp = nowIso(),
                        errorType = "ERROR",
                        errorDetail = "Offline cmd=0x${ev.cmd.toString(16)}"
                    ))
                }
                is Gs805ViewModel.MachineEvent.ErrorCode -> {
                    // ev.code 값을 확인하여 로그 내용을 분기
                    val detailMessage = if (ev.code == -1) {
                        "SerialCommunicationError" // 통신 시스템 자체 오류
                    } else {
                        "MachineErrorCode=0x${ev.code.toString(16)}" // 장치가 보고한 특정 에러
                    }

                    sendLog(api, apiKey, KioskLogPayload(
                        errorId = newUniqueId(),
                        timestamp = nowIso(),
                        errorType = "ERROR",
                        errorDetail = detailMessage // 분기 처리된 메시지 사용
                    ))
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
