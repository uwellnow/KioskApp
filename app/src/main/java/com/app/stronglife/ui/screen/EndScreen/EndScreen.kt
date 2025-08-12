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
import com.app.stronglife.viewmodel.Gs805ViewModel
import com.app.stronglife.viewmodel.Gs805ViewModel.MachineEvent

@Composable
fun EndScreen(
    navController: NavController,
    vm: Gs805ViewModel = viewModel()
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

    // 1) 시리얼 시작 (한 번만)
    LaunchedEffect(Unit) {
        vm.startSerial()
    }

    // 2) 장치 이벤트 수신 → 제조 완료 시 화면 전환
    LaunchedEffect(Unit) {
        vm.events.collectLatest { ev ->
            when (ev) {
                is MachineEvent.DrinkCompleted -> {
                    // UX 상 약간의 딜레이 후 홈으로
                    delay(300)
                    navController.navigate("first")
                }
                is MachineEvent.CupDropped -> {
                    println("Cup dropped")
                }
                is MachineEvent.Offline -> {
                    println("OFFLINE for cmd=0x${ev.cmd.toString(16)}")
                }
                is MachineEvent.ErrorCode -> {
                    println("ERROR CODE: ${ev.code}")
                }
            }
        }
    }

    // 3) 테스트 시퀀스 (3계열): 0x0C → 0x15 → 0x01
    //    * 나중에 인자로 받을 예정이라 지금은 하드코딩 값으로 동작 확인용
    LaunchedEffect(Unit) {
        runCatching {
            // (a) 상태 확인 (0x0C)
            val err = vm.queryErrorCode()
            println("queryErrorCode() = 0x${err.toString(16)}")

            // (b) 레시피 저장 (0x15, 3계열)
            //    Drink_NO: 예시로 '차가운 1번' 0x11 사용 (0x11~0x17: 냉음료)
            val drinkNo = 0x11
            //    8채널 값: (분말, 물). 단위는 장치 스펙(보통 0.1s / 유량계 있으면 g).
            //    테스트로 1번 채널만 80/80, 나머지 0/0
            val slots = listOf(
                80 to 80, // 1
                0 to 0,   // 2
                0 to 0,   // 3
                0 to 0,   // 4
                0 to 0,   // 5
                0 to 0,   // 6
                0 to 0,   // 7
                0 to 0    // 8
            )
            val okRecipe = vm.saveRecipe3(drinkNo, slots)
            println("saveRecipe3() = $okRecipe")
            check(okRecipe) { "레시피 저장 실패" }

            // (c) 즉시 제조 (0x01, LocalOrCmd=0x02: 명령으로 바로 제조)
            val okMake = vm.makeDrinkNow(drinkNo, localOrCmd = 0x02)
            println("makeDrinkNow() = $okMake")
            check(okMake) { "제조 시작 실패" }

            // 완료 신호는 비동기 0x0C 이벤트로 오고,
            // 위 collectLatest에서 DrinkCompleted를 받으면 네비게이션함.
        }.onFailure { e ->
            // onFailure 유지: 테스트 중 오류 로그만
            e.printStackTrace()
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
