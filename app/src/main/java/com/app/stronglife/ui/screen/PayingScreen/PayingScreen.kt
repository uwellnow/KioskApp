package com.app.stronglife.ui.screen.PayingScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.R
import com.app.stronglife.data.model.UserPurchase
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.cardPayGray
import com.app.stronglife.ui.theme.midGray
import com.app.stronglife.viewmodel.UserCodeViewModel
import kotlinx.coroutines.delay
import retrofit2.Response



@Composable
fun PayingScreen(viewModel: CartViewModel, userViewModel: UserCodeViewModel,navController: NavController) {
    val density = LocalDensity.current
    val widDp = with(density) {1231f.toDp()}
    val heightDp = with(density) {824f.toDp()}
    val roundDp = with(density) {32f.toDp()}
    val textSp = with(density) {36f.toSp()}
    val spaceDp = with(density) {81f.toDp()}
    val space2Dp = with(density) {134f.toDp()}

    val cartItems by viewModel.cartItems
    val loginData = userViewModel.loginResponse.value

    LaunchedEffect(Unit) {
        val userPurchase = UserPurchase(
            productId = cartItems.map { it.product.id },
            productCount = cartItems.map { it.quantity },
            userId = "asdf",
            userCode = "asdf",
        )

        try {
            val response = RetrofitClient.api.postPurchaseProduct(
                apiKey = "발급받은_API_KEY",
                body = userPurchase
            )

            if (response.isSuccessful) {
                // 성공 시 처리
                navController.navigate("end")
            } else {
                // 실패 시 처리
                // 예: 로그 출력
                println("결제 요청 실패: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // 네트워크 오류 처리
        }
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        TopBar(4, listOf("섭취시점 선택", "메뉴선택", "주문 확인", "결제하기"), navController, cartViewModel = viewModel)

        Spacer(modifier = Modifier.height(spaceDp))
        Column (
            modifier = Modifier
                .width(widDp)
                .height(heightDp)
                .padding(top = space2Dp),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "결제가 진행 중이에요",
                style = TextStyle(
                    fontSize = textSp,
                    fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                    fontWeight = FontWeight.Bold,
                    color = midGray
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(1500)
        navController.navigate("end")
    }
}
