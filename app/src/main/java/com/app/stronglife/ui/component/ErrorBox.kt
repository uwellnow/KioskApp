package com.app.stronglife.ui.component

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.R
import com.app.stronglife.data.model.UserPurchase
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.cardPayGray
import com.app.stronglife.ui.theme.errorGray
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.midGray
import com.app.stronglife.ui.theme.shadowGray
import com.app.stronglife.viewmodel.UserCodeViewModel
import kotlinx.coroutines.delay


@Composable
fun ErrorBox(errorMsg: String, desMsg: String, navController: NavController) {
    val density = LocalDensity.current
    val widDp = with(density) {1231f.toDp()}
    val heightDp = with(density) {824f.toDp()}
    val roundDp = with(density) {32f.toDp()}
    val ErrorSp = with(density) {56f.toSp()}
    val DesSp = with(density) {28f.toSp()}
    val spaceDp = with(density) {56f.toDp()}
    val btnSp = with(density) {28f.toSp()}
    val round2dp = with(density) {48f.toDp()}
    val space2Dp = with(density) {135f.toDp()}
    val space3Dp = with(density) {257f.toDp()}

    val borderRadiusPx = with(density) { roundDp.toPx() }
    val blurRadiusPx = with(density) { 24.dp.toPx() }



    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Column (
            modifier = Modifier
                .width(widDp)
                .height(heightDp)
                .drawBehind {
                    drawIntoCanvas { canvas ->
                        val paint = Paint().asFrameworkPaint().apply {
                            color = shadowGray.toArgb()
                            maskFilter = android.graphics.BlurMaskFilter(borderRadiusPx, android.graphics.BlurMaskFilter.Blur.NORMAL)
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
        ) {
            Text(
                text = "["+ errorMsg+"]",
                style = TextStyle(
                    fontSize = ErrorSp,
                    fontFamily = FontFamily(Font(R.font.sfpro_semibold)),
                    fontWeight = FontWeight.Bold,
                    color = black
                ),
                modifier = Modifier.padding(top= space3Dp)
            )
            Spacer(modifier = Modifier.height(spaceDp))
            Text(
                text = desMsg,
                style = TextStyle(
                    fontSize = DesSp,
                    fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                    color = errorGray
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(space2Dp))
            Box(
                modifier = Modifier
                    .background(color = mainRed, shape = RoundedCornerShape(round2dp))
                    .clickable {
                        navController.navigate("hello")
                    },

                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "처음으로 이동",
                    style = TextStyle(
                        fontSize = btnSp,
                        fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                        color = Color.White
                    ),
                    modifier = Modifier.padding(horizontal = 70.dp, vertical = 30.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=1920px,height=1080px,dpi=81")
@Composable
fun ErrorBoxPreview() {
    ErrorBox("결제 오류", "회원 정보를 찾을 수 없습니다.\n유웰나우 회원이 맞는지 확인해 주세요", navController = rememberNavController())
}
