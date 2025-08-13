package com.app.stronglife.ui.screen.menuScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.stronglife.R
import com.app.stronglife.ui.screen.firstScreen.customShadow
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.lightRed
import com.app.stronglife.ui.theme.midGray

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProductDetail (image:String, title:String, nut:String , onClose: () -> Unit, onAddToCart: () -> Unit, onGoCart: () -> Unit) {
    val density = LocalDensity.current
    val widthtoDp = with(density) {1649f.toDp()}
    val heighttoDp = with(density) {776.toDp()}
    val titletoSp = with(density) {40f.toSp()}
    val nuttoSp = with(density) {24f.toSp()}
    val imagetoDp = with(density) {320f.toDp()}
    val roundtoDp = with(density) {20f.toDp()}
    val imagetoTextDp = with(density) {49f.toDp()}
    val blurRadiusPx = with(density) { 24.dp.toPx() }
    val spacertoDp = with(density) {30f.toDp()}

        Column(
            modifier = Modifier
                .width(widthtoDp)
                .height(heighttoDp)
                .drawBehind {
                    drawIntoCanvas { canvas ->
                        val paint = Paint().asFrameworkPaint().apply {
                            color = lightRed.toArgb()
                            maskFilter = android.graphics.BlurMaskFilter(blurRadiusPx, android.graphics.BlurMaskFilter.Blur.NORMAL)
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
                .background(Color.White, RoundedCornerShape(roundtoDp))
                .padding(spacertoDp) // 전체 여백
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = image,
                    contentDescription = title,
                    modifier = Modifier
                        .width(imagetoDp)
                        .height(imagetoDp)
                        .padding(top = imagetoTextDp)
                )
                Column(
                    modifier = Modifier.padding(start = imagetoTextDp, top = imagetoTextDp * 2)
                ) {
                    Text(
                        text = title,
                        style = TextStyle(
                            fontSize = titletoSp,
                            fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                            fontWeight = FontWeight.Bold,
                            color = black
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = nut,
                        style = TextStyle(
                            fontSize = nuttoSp,
                            fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                            fontWeight = FontWeight.Normal,
                            color = midGray
                        )
                    )
                }
            }

            // 남은 공간 차지해서 버튼을 아래로 밀기
            Spacer(modifier = Modifier.weight(1f))

            // 하단 고정 버튼
            MenuScreenBtn(
                onBackClick = onClose,
                onCartClick = {
                    onAddToCart()
                    onClose()
                    onGoCart()
                }
            )
    }


    }

