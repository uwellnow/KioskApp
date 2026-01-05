package com.app.stronglife.ui.screen.EndScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.stronglife.R
import com.app.stronglife.ui.theme.Stronglife
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.shadowGray

@Composable
fun QuestionBarBox(
    question: ScoreQuestion,
    questionIndex: Int,
    onAnswered: (Int) -> Unit
) {
    val density = LocalDensity.current
    val boxWidth = with(density) {1498f.toDp()}
    val boxHeight = with(density) {448f.toDp()}
    val titleTextSp = with(density) {44f.toSp()}
    val indexTextSp = with(density) {36f.toSp()}
    val dotSize = with(density) {30f.toDp()}
    val selectedDotSize = with(density) {32f.toDp()}
    val heightSpaceDp = with(density) {32f.toDp()}
    val blurRadiusPx = with(density) { 7.dp.toPx() }
    val roundDp = with(density) {20f.toDp()}
    val descTextSp = with(density) {28f.toSp()}

    var selectedScore by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(questionIndex) {
        selectedScore = null
    }

    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(boxWidth, boxHeight)
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
                .background(color = Color.White, RoundedCornerShape(roundDp))
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = question.title,
                        fontFamily = Stronglife,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = titleTextSp,
                        color = black
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Text(
                        text = buildAnnotatedString {
                            append("(")
                            withStyle(
                                style = SpanStyle(color = mainRed)
                            ) {
                                append(question.index.toString())
                            }
                            append("/3)")
                        },
                        fontFamily = Stronglife,
                        fontWeight = FontWeight.Medium,
                        fontSize = indexTextSp,
                        color = Color(0xFFD1D5DC)
                    )
                }

                Spacer(modifier = Modifier.height(heightSpaceDp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .background(mainRed.copy(alpha = 0.5f))
                    )

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        (1..5).forEach {
                            score ->
                            Column (
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Image(
                                    painter = painterResource(
                                        id = if (selectedScore == score) R.drawable.dot_selected else R.drawable.dot_noselected,
                                    ),
                                    contentDescription = "$score 점",
                                    modifier = Modifier
                                        .size(if (selectedScore == score) dotSize else selectedDotSize)
                                        .clickable {
                                            selectedScore = score
                                            onAnswered(score)
                                        }
                                )

                                Spacer(modifier = Modifier.height(heightSpaceDp))

                                Text(
                                    text = "${score}점",
                                    fontFamily = Stronglife,
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

@Preview(device = "spec:width=1920px,height=1080px,dpi=82")
@Composable
fun QuestionBarBoxPreview() {
    QuestionBarBox(question = ScoreQuestion(
            index = 3,
            title = "유웰나우를 주변 분께 추천할 의향이 있으신가요?",
            labels = listOf(
                "추천하지 않을래요",
                "잘 모르겠어요",
                "보통이에요",
                "추천할 것 같아요",
                "꼭 추천할래요"
            )
    ),
        questionIndex = 0,
        onAnswered = {})
}