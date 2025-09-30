package com.app.stronglife.ui.screen.RecipeScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.stronglife.R
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.screen.PayScreen.CouponInputCard
import com.app.stronglife.ui.screen.PayScreen.PaymentTab
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.cardPayGray
import com.app.stronglife.ui.theme.lightGray
import com.app.stronglife.ui.theme.lightRed
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.midGray
import com.app.stronglife.ui.theme.shadowGray
import com.app.stronglife.viewmodel.UserCodeViewModel

@Composable
fun RecipeScreen(navController: NavController, cartViewModel: CartViewModel, userCodeViewModel: UserCodeViewModel, apiKey: String) {

    val density = LocalDensity.current

    val barbtnSpace = with(density) {60f.toDp()}
    val widDp = with(density) {1231f.toDp()}
    val heightDp = with(density) {824f.toDp()}
    val roundDp = with(density) {32f.toDp()}
    val titleSp = with(density) {36f.toSp()}
    val spacerDp = with(density) {10f.toDp()}
    val descSp = with(density) {24f.toSp()}
    val spaceDp = with(density) { 30f.toDp()}

    val blurRadiusPx = with(density) { 24.dp.toPx() }

    var selected by remember { mutableStateOf("QR") }

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopBar(1,  listOf("추천받은 레시피 불러오기"), navController, cartViewModel)

        Spacer(modifier = Modifier.height(barbtnSpace))

        Column (
            modifier = Modifier
                .width(widDp)
                .height(heightDp)
                .drawBehind {
                    drawIntoCanvas { canvas ->
                        val paint = Paint().asFrameworkPaint().apply {
                            color = shadowGray.toArgb()
                            maskFilter = android.graphics.BlurMaskFilter(roundDp.toPx(), android.graphics.BlurMaskFilter.Blur.NORMAL)
                        }
                        canvas.nativeCanvas.drawRoundRect(
                            0f,
                            0f,
                            size.width,
                            size.height,
                            blurRadiusPx,
                            blurRadiusPx,
                            paint
                        )
                    }
                }
                .background(color = Color.White, shape = RoundedCornerShape(roundDp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Row (
                modifier = Modifier.fillMaxWidth()
            ){
                PaymentTab(
                    text = "QR코드 스캔",
                    isSelected = selected == "QR",
                    onClick = { selected = "QR"},
                )
                PaymentTab(
                    text = "휴대폰 번호로 조회",
                    isSelected = selected == "phone",
                    onClick = { selected = "phone"},
                )


            }
            Spacer(modifier = Modifier.height(spaceDp))
            Text(
                text = "앱에서 발급받은 QR코드를 스캔하세요",
                style = TextStyle(
                    fontSize = titleSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    color = midGray
                )
            )
            Spacer(modifier = Modifier.height(spacerDp))

            Text(
                text = "아직 추천검사를 진행하지 않았다면?",
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
    }
}

