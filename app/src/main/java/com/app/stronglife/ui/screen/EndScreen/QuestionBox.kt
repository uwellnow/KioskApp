package com.app.stronglife.ui.screen.EndScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.stronglife.ui.theme.shadowGray

@Composable
fun QuestionBox() {
    val density = LocalDensity.current
    val boxWidth = with(density) {1498f.toDp()}
    val boxHeight = with(density) {448f.toDp()}
    val titleTextSp = with(density) {44f.toSp()}
    val indexTextSp = with(density) {36f.toSp()}
    val normalTextSp = with(density) {30f.toSp()}
    val smallTextSp = with(density) { 28f.toSp()}
    val roundDp = with(density) {20f.toDp()}

    val blurRadiusPx = with(density) { 7.dp.toPx() }

    var isAnswered by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier.size(boxWidth, boxHeight)
                .background(color = Color.White, RoundedCornerShape(roundDp))
                .drawBehind {
                    drawIntoCanvas { canvas ->
                        val paint = Paint().asFrameworkPaint().apply {
                            color = shadowGray.toArgb()
                            maskFilter = android.graphics.BlurMaskFilter(
                                blurRadiusPx,
                                android.graphics.BlurMaskFilter.Blur.NORMAL
                            )
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
        ) {

        }

        Spacer(modifier = Modifier.height(56.dp))

        PrevNextBox(isAnswered = isAnswered)
    }

}

@Preview( device = "spec:width=1920px,height=1080px,dpi=82")
@Composable
fun QuestionBoxPreview() {
    QuestionBox()
}