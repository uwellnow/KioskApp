package com.app.stronglife.ui.screen.PayScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.app.stronglife.ui.theme.background
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.lightGray
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.midGray
import com.app.stronglife.viewmodel.UserCodeViewModel

@Composable
fun CouponInputCard(
    navController: NavController,
    viewModel: UserCodeViewModel,
    apiKey: String,
    cartViewModel: CartViewModel,
    onCouponSuccess: () -> Unit
) {
    val density = LocalDensity.current
    val widDp = with(density) { 584f.toDp() }
    val textSp = with(density) { 36f.toSp() }
    val roundDp = with(density) { 12f.toDp() }
    val boxWidDp = with(density) { 97f.toDp() }
    val boxHeiDp = with(density) { 44f.toDp() }
    val boxTextSp = with(density) { 24f.toSp() }
    val spacerDp = with(density) { 60f.toDp() }
    val spacer2Dp = with(density) { 17f.toDp() }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(widDp).padding(horizontal = 40.dp)
        ) {
            Text(
                text = if (viewModel.userCode.value.isEmpty()) "쿠폰 코드를 입력하세요" else viewModel.userCode.value,
                style = TextStyle(
                    fontSize = textSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontWeight = FontWeight.Medium,
                    color = lightGray
                )
            )

            Spacer(modifier = Modifier.width(spacerDp))

            val isFilled = viewModel.userCode.value.isNotEmpty()

            Box(
                modifier = Modifier
                    .background(
                        if (viewModel.userCode.value.isNotEmpty()) mainRed else background,
                        shape = RoundedCornerShape(roundDp)
                    )
                    .size(boxWidDp, boxHeiDp)
                    .clickable(enabled = isFilled) {
                        if (viewModel.userCode.value.isNotEmpty()) {
                            // 쿠폰 구매 요청
                            val productIds = cartViewModel.cartItems.value.map { it.product.id }
                            val productCounts = cartViewModel.cartItems.value.map { it.quantity }
                            
                            viewModel.purchaseByCoupon(
                                apiKey = apiKey,
                                couponCode = viewModel.userCode.value,
                                productIds = productIds,
                                productCounts = productCounts
                            ) { success ->
                                if (success) {
                                    onCouponSuccess()
                                }
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "사용",
                    style = TextStyle(
                        fontSize = boxTextSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontWeight = FontWeight.Medium,
                        color = if (viewModel.userCode.value.isNotEmpty()) Color.White else midGray
                    )
                )
            }
        }

        Divider(
            modifier = Modifier.width(widDp).padding(top = spacer2Dp),
            color = lightGray
        )

        Spacer(modifier = Modifier.height(spacerDp))

        KeyPad(
            onNumberClick = { digit -> viewModel.addDigit(digit) },
            onDeleteClick = { viewModel.removeLast() },
            onClearClick = { viewModel.clear() }
        )
    }
}

@Preview(showBackground = true, device = "spec:width=1920px,height=1080px,dpi=82")
@Composable
fun CouponInputCardPreview() {
    CouponInputCard(
        navController = rememberNavController(),
        viewModel = UserCodeViewModel.getInstance(com.app.stronglife.data.remote.RetrofitClient.api),
        apiKey = "test",
        cartViewModel = CartViewModel(),
        onCouponSuccess = {}
    )
}

