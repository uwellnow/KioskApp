package com.app.stronglife.ui.screen.PayScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.R
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.screen.PayingScreen.MemberErrorBox
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.cardPayGray
import com.app.stronglife.ui.theme.lightGray
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.viewmodel.UserCodeViewModel
import com.app.stronglife.viewmodel.UserCodeViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun OrderNumScreen(
    navController: NavController, 
    apiKey: String,
    cartViewModel: CartViewModel = viewModel(),
    userCodeViewModel: UserCodeViewModel = UserCodeViewModel.getInstance(RetrofitClient.api)
) {

    val density = LocalDensity.current
    val barbtnSpace = with(density) {81f.toDp()}
    val widDp = with(density) {1231f.toDp()}
    val heightDp = with(density) {824f.toDp()}
    val roundDp = with(density) {32f.toDp()}
    val spaceDp = with(density) {61f.toDp()}


    val titleSp = with(density) {36f.toSp()}
    val spacerDp = with(density) {16f.toDp()}
    val descSp = with(density) {20f.toSp()}

    // 화면이 처음 로드될 때 userCode 초기화
    LaunchedEffect(Unit) {
        userCodeViewModel.clear()
        userCodeViewModel.clear404Error()
    }

    // 404 오류 시 MemberErrorBox 표시
    if (userCodeViewModel.is404Error.value) {
        MemberErrorBox(navController = navController)
        return
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ){
        TopBar(4, listOf("섭취시점 선택", "메뉴선택", "주문 확인", "결제하기"), navController, cartViewModel = cartViewModel)
        Spacer(modifier = Modifier.height(barbtnSpace))
        
        Column (
            modifier = Modifier
                .width(widDp)
                .height(heightDp)
                .background(color = Color.White, shape = RoundedCornerShape(roundDp))
                .border(2.dp, cardPayGray, RoundedCornerShape(roundDp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = "주문번호를 입력하세요",
                style = TextStyle(
                    fontSize = titleSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    color = black
                )
            )
            Spacer(modifier = Modifier.height(spacerDp))

            Text(
                text = "결제 완료 후 카카오톡 채널로 발송된 메시지를 확인해 주세요",
                style = TextStyle(
                    fontSize = descSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = lightGray
                )
            )

            Spacer(modifier = Modifier.height(spaceDp))
            
            PhonePayCard(
                navController = navController,
                viewModel = userCodeViewModel,
                apiKey = apiKey,
                onUserFound = { navController.navigate("userInfo") },
                cartViewModel = cartViewModel
            )
        }
        Spacer(modifier = Modifier.weight(1f)) // 아래로 밀기

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "* 현재 카드결제, 간편결제는 지원되지 않습니다. 추후 업데이트 예정입니다.",
                style = TextStyle(
                    fontSize = descSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = mainRed
                )
            )
        }


    }
}

@Preview(showBackground = true, device = "spec:width=1920px,height=1080px,dpi=82")
@Composable
fun OrderNumScreenPreview() {
    OrderNumScreen(navController = rememberNavController(), apiKey = "test")
}

