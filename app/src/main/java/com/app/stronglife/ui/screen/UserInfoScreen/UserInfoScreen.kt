package com.app.stronglife.ui.screen.UserInfoScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.R
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.ui.component.ErrorBox
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.screen.PayScreen.UserBox
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.cardPayGray
import com.app.stronglife.ui.theme.desc2Gray
import com.app.stronglife.ui.theme.descGray
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.viewmodel.UserCodeViewModel
import com.app.stronglife.viewmodel.UserCodeViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun UserInfoScreen(
    navController: NavController, 
    apiKey: String,
    cartViewModel: CartViewModel = viewModel()
) {
    val userCodeViewModel = UserCodeViewModel.getInstance(RetrofitClient.api)
    val loginResponse = userCodeViewModel.loginResponse.value

    // 화면 진입 시 결제 에러 상태 초기화
    LaunchedEffect(Unit) {
        userCodeViewModel.clearPurchaseError()
    }

    val density = LocalDensity.current
    val barbtnSpace = with(density) {27f.toDp()}
    val widDp = with(density) {374f.toDp()}
    val heightDp = with(density) {91f.toDp()}
    val roundDp = with(density) {46f.toDp()}

    // 결제 실패 시 ErrorBox 표시
    if (userCodeViewModel.isPurchaseError.value) {
        ErrorBox(
            errorMsg = "재고 부족", //Todo: 재고 부족으로 변경 -> 409 에러 (컵이랑 물 구분)
            desMsg = "결제 중 오류가 발생했습니다. 다시 시도해 주세요.",
            navController = navController
        )
        return
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopBar(4, listOf("섭취시점 선택", "메뉴선택", "주문 확인", "결제하기"), navController, cartViewModel = cartViewModel)
        
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){

            val titleSp = with(density) {70f.toSp()}
            val title2descDp = with(density) {30f.toDp()}
            val descSp = with(density) {36f.toSp()}
            val desc2infoDp = with(density) {80f.toDp()}
            val info2btnDp = with(density) {141f.toDp()}

            val btnTextSp = with(density) {32f.toSp()}

            Text(
                text = "조회된 회원 정보를 확인해 주세요",
                style = TextStyle(
                    fontSize = titleSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    color = black
                )
            )

            Spacer(modifier = Modifier.height(title2descDp))

            Text(
                text = "회원 정보가 일치하면, 결제를 완료해 주세요.",
                style = TextStyle(
                    fontSize = descSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = descGray
                )
            )


            Spacer(modifier = Modifier.height(desc2infoDp))
            
            UserBox(
                showInfo = userCodeViewModel.loginResponse.value != null,
                loginResponse = userCodeViewModel.loginResponse.value,
                cartViewModel = cartViewModel
            )
            Spacer(modifier = Modifier.height(info2btnDp))

            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
            ){
                Box(
                    modifier = Modifier.size(widDp,heightDp)
                        .background(Color.Transparent, RoundedCornerShape(roundDp))
                        .border(2.dp, mainRed, RoundedCornerShape(roundDp))
                        .clickable{navController.popBackStack()},
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "제 정보가 아니예요",
                        style = TextStyle(
                            fontSize = btnTextSp,
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            color = mainRed
                        )
                    )
                }

                Spacer(modifier = Modifier.width(title2descDp))

                Box(
                    modifier = Modifier.size(widDp,heightDp)
                        .background(mainRed, RoundedCornerShape(roundDp))
                        .border(2.dp, mainRed, RoundedCornerShape(roundDp))
                        .clickable{
                            // 장바구니 정보 가져오기
                            val cartItems = cartViewModel.cartItems.value
                            val productIds = cartItems.map { it.product.id }
                            val productCounts = cartItems.map { it.quantity }
                            
                            // 구매 요청
                            userCodeViewModel.purchaseProductByOrder(
                                apiKey = apiKey,
                                orderNumber = userCodeViewModel.userCode.value,
                                productIds = productIds,
                                productCounts = productCounts
                            ) { success ->
                                if (success) {
                                    navController.navigate("paying")
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "네, 맞아요",
                        style = TextStyle(
                            fontSize = btnTextSp,
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=1920px,height=1080px,dpi=82")
@Composable
fun UserInfoScreenPreview() {
    UserInfoScreen(navController = rememberNavController(), apiKey = "test")
}
