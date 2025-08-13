package com.app.stronglife.ui.screen.firstScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.R
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.lightGray
import com.app.stronglife.ui.theme.lightRed
import com.app.stronglife.ui.theme.mainRed

fun Modifier.customShadow(
    color: Color,
    blurRadius: Float,
    borderRadius: Float,
    offsetX: Float,
    offsetY: Float
) = this.then(
    Modifier.drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint().asFrameworkPaint().apply {
                this.color = android.graphics.Color.TRANSPARENT
                setShadowLayer(blurRadius, offsetX, offsetY, color.toArgb())
            }
            canvas.nativeCanvas.drawRoundRect(
                0f,
                0f,
                size.width,
                size.height,
                borderRadius,
                borderRadius,
                paint
            )
        }
    }
)

@Composable
fun TimeSelectBtn(time: String, description: String, english: String, navController: NavController) {
    val density = LocalDensity.current

    val widthInDp = with(density) { 568.toDp() }
    val heightInDp = with(density) { 443.toDp() }
    val roundInDp = with(density) { 20f.toDp() }
    val startInDp = with(density) { 30f.toDp() }
    val midInDp = with(density) { 20f.toDp() }
    val topInDp = with(density) { 45f.toDp() }
    val timeInSp = with(density) { 36.toSp() }
    val desInSp = with(density) { 24.toSp() }
    val engInSp = with(density) { 124.toSp() }


    val blurRadiusPx = with(density) { 24.dp.toPx() }

    Box(
        modifier = Modifier
            .width(widthInDp)
            .height(heightInDp)
            .drawBehind {
                drawIntoCanvas { canvas ->
                    val paint = Paint().asFrameworkPaint().apply {
                        color = lightRed.toArgb()
                        maskFilter = android.graphics.BlurMaskFilter(roundInDp.toPx(), android.graphics.BlurMaskFilter.Blur.NORMAL)
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
            .background(Color.White, RoundedCornerShape(roundInDp))
            .clickable { navController.navigate("menu") }
    ) {
        Column(
            modifier = Modifier
                .padding(start = startInDp, top = topInDp)
        ) {
            Text(
                text = time,
                style = TextStyle(
                    fontSize = timeInSp,
                    fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                    color = black
                )
            )
            Spacer(modifier = Modifier.height(midInDp))
            Text(
                text = description,
                style = TextStyle(
                    fontSize = desInSp,
                    lineHeight = desInSp * 1.2,
                    fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                    fontWeight = FontWeight.Normal,
                    color = lightGray
                )
            )
            Spacer(modifier = Modifier.height(midInDp))
            Text(
                text = english,
                style = TextStyle(
                    fontSize = engInSp,
                    lineHeight = engInSp * 0.8,
                    letterSpacing = (-4).sp,
                    fontFamily = FontFamily(Font(R.font.sfpro_black)),
                    fontWeight = FontWeight.Black,
                    color = lightRed
                )
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=1920px,height=1080px,dpi=81")
@Composable
fun BtnPreview() {
    TimeSelectBtn(
        "운동 후",
        "근육 회복 및 합성 촉진, 빠른 회복,\n글리코겐 보충에 도움을 줄 수 있습니다",
        "Post-\nworkout",
        navController = rememberNavController()
    )
}
