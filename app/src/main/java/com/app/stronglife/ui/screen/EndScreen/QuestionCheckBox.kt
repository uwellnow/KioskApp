package com.app.stronglife.ui.screen.EndScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.app.stronglife.ui.theme.Stronglife
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.shadowGray

@Composable
fun QuestionCheckBox(question: CheckQuestion, questionIndex: Int, onAnswered: (String) -> Unit) {
    val density = LocalDensity.current
    val boxWidth = with(density) {1498f.toDp()}
    val boxHeight = with(density) {448f.toDp()}
    val titleTextSp = with(density) {44f.toSp()}
    val indexTextSp = with(density) {36f.toSp()}
    val heightSpaceDp = with(density) {32f.toDp()}
    val roundDp = with(density) {20f.toDp()}

    val blurRadiusPx = with(density) { 7.dp.toPx() }

    var isAnswered by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(questionIndex) {
        selectedIndex = null
        isAnswered = false
    }

    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
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
            Column (
                modifier = Modifier.fillMaxSize().padding(horizontal = 55.dp, vertical = 60.dp),
                verticalArrangement = Arrangement.Center,

                ){
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
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
                            append("/${QuestionDatas.size})")
                        },
                        fontFamily = Stronglife,
                        fontWeight = FontWeight.Medium,
                        fontSize = indexTextSp,
                        color = Color(0xFFD1D5DC)
                    )
                }

                Spacer(modifier = Modifier.height(heightSpaceDp))

                Column (
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ){
                    // 선택지를 2개씩 행으로 나누어 표시 (동적 처리)
                    question.choices.chunked(2).forEach { rowChoices ->
                        Row (
                            horizontalArrangement = Arrangement.spacedBy(18.dp)
                        ){
                            rowChoices.forEachIndexed { rowIndex, choice ->
                                val globalIndex = question.choices.indexOf(choice)

                                CheckItem(
                                    choice = choice,
                                    isSelected = selectedIndex == globalIndex
                                ) {
                                    selectedIndex = globalIndex
                                    isAnswered = true
                                    onAnswered(choice)
                                }
                            }
                            // 홀수 개일 경우 빈 공간 추가
                            if (rowChoices.size == 1) {
                                Spacer(modifier = Modifier.width(685.dp + 18.dp))
                            }
                        }
                    }
                }

            }
        }

        Spacer(modifier = Modifier.height(56.dp))

    }

}
