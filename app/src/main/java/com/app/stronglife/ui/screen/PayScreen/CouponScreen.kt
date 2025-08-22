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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.stronglife.R
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.component.ErrorBox
import com.app.stronglife.ui.screen.PayingScreen.MemberErrorBox
import com.app.stronglife.ui.screen.PayScreen.CouponInputCard
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.cardPayGray
import com.app.stronglife.ui.theme.lightGray
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.viewmodel.UserCodeViewModel
import com.app.stronglife.viewmodel.UserCodeViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun CouponScreen(
    navController: NavController,
    apiKey: String,
    cartViewModel: CartViewModel = viewModel()
) {
    val userCodeViewModel: UserCodeViewModel = UserCodeViewModel.getInstance(RetrofitClient.api)

    // 화면 진입 시 모든 상태 초기화


    // 화면을 떠날 때 에러 상태 초기화
    DisposableEffect(Unit) {
        onDispose {
            userCodeViewModel.errorState.value = UserCodeViewModel.UiError.None
            userCodeViewModel.clear() // 입력된 번호 초기화
        }
    }

    val density = LocalDensity.current
    val barbtnSpace = with(density) {81f.toDp()}
    val widDp = with(density) {1231f.toDp()}
    val heightDp = with(density) {824f.toDp()}
    val roundDp = with(density) {32f.toDp()}
    val spaceDp = with(density) {61f.toDp()}


    val titleSp = with(density) {36f.toSp()}
    val spacerDp = with(density) {16f.toDp()}
    val descSp = with(density) {20f.toSp()}

    val errorState by userCodeViewModel.errorState

    // 에러 상태에 따른 처리
    when (val error = errorState) {
        is UserCodeViewModel.UiError.None -> {}
        is UserCodeViewModel.UiError.NotFound -> {
            MemberErrorBox(onConfirm = {
                userCodeViewModel.errorState.value = UserCodeViewModel.UiError.None
            })
            return
        }
        is UserCodeViewModel.UiError.InsufficientBalance -> {
            ErrorBox("결제 실패", "쿠폰이 유효하지 않습니다") {
                userCodeViewModel.errorState.value = UserCodeViewModel.UiError.None
                navController.navigate("cart")
            }
            return
        }
        is UserCodeViewModel.UiError.OutOfStock -> {
            ErrorBox("결제 실패", "재고가 부족합니다") {
                userCodeViewModel.errorState.value = UserCodeViewModel.UiError.None
            }
            return
        }
        is UserCodeViewModel.UiError.Generic -> {
            ErrorBox("오류", error.message) {
                userCodeViewModel.errorState.value = UserCodeViewModel.UiError.None
            }
            return
        }
        is UserCodeViewModel.UiError.Exception -> {
            ErrorBox("예외 발생", error.throwable.message ?: "알 수 없는 오류") {
                userCodeViewModel.errorState.value = UserCodeViewModel.UiError.None
            }
            return
        }
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
                text = stringResource(R.string.pay_coupon_title),
                style = TextStyle(
                    fontSize = titleSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    color = black
                )
            )
            Spacer(modifier = Modifier.height(spacerDp))

            Text(
                text = stringResource(R.string.pay_coupon_desc),
                style = TextStyle(
                    fontSize = descSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = lightGray
                )
            )

            Spacer(modifier = Modifier.height(spaceDp))

            CouponInputCard(
                navController = navController,
                viewModel = userCodeViewModel,
                apiKey = apiKey,
                cartViewModel = cartViewModel,
                onCouponSuccess = { navController.navigate("paying") }
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
                text = stringResource(R.string.pay_order_add),
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
fun CouponPreview() {
    CouponScreen(navController = rememberNavController(), apiKey = "test")
}
