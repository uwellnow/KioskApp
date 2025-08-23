package com.app.stronglife.ui.screen.EndScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.stronglife.R
import com.app.stronglife.ui.theme.mainRed
import kotlinx.coroutines.delay
import kotlin.math.min
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.max

@Composable
fun MakingBar(
    modifier: Modifier = Modifier,
    stepSeconds: Int = 20,
    activeColor: Color = mainRed,
    idleColor: Color = Color(0xFFAFAFAF),
    onFinished: (() -> Unit)? = null
) {
    val titles = listOf("물\n보충", "보충제 투출", "믹싱", "제조 완료")
    var completed by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        repeat(titles.size) {
            delay(stepSeconds * 1000L)
            completed += 1
        }
        onFinished?.invoke()
    }

    val density = LocalDensity.current
    val circleDp = with(density) { 100f.toDp() }
    val numSp = with(density) { 56f.toSp() }
    val textSp = with(density) { 36f.toSp() }
    val spaceDp = with(density) { 54f.toDp() }
    val sidePad = 32.dp

    Column(modifier = modifier.fillMaxWidth()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(circleDp)
                .padding(horizontal = sidePad)
        ) {
            // 배경 연결선들 - 원보다 먼저 그려서 뒤에 위치
            Canvas(modifier = Modifier.fillMaxSize()) {
                val circleDiameter = circleDp.toPx()
                val totalCircleWidth = circleDiameter * titles.size
                val totalLineWidth = size.width - totalCircleWidth
                val lineLength = totalLineWidth / (titles.size - 1)
                val centerY = size.height / 2f
                val radius = circleDp.toPx() / 2f

                for (i in 0 until titles.size - 1) {
                    // 원의 테두리에서 시작/끝나도록 조정
                    val startX = circleDiameter * (i + 1) + lineLength * i
                    val endX = startX + lineLength
                    val lineColor = if (completed > i) activeColor else idleColor

                    if (startX < endX) { // 유효한 선만 그리기
                        drawLine(
                            color = lineColor,
                            start = Offset(startX, centerY),
                            end = Offset(endX, centerY),
                            strokeWidth = 4.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            // 원들 - 연결선 위에 그려져서 선을 가림
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                titles.forEachIndexed { index, _ ->
                    val state = when {
                        index < completed -> StepState.DONE
                        index == min(completed, titles.lastIndex) && completed != titles.size -> StepState.ACTIVE
                        else -> StepState.IDLE
                    }

                    when (state) {
                        StepState.DONE -> Box(
                            modifier = Modifier
                                .size(circleDp)
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            // 배경 원 (체크 아이콘이 투명한 경우를 대비)
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .border(2.dp, activeColor, CircleShape)
                            )
                            Image(
                                painter = painterResource(id = R.drawable.check),
                                contentDescription = "done",
                                modifier = Modifier.size(circleDp)
                            )
                        }
                        StepState.ACTIVE, StepState.IDLE -> {
                            val border = if (state == StepState.ACTIVE) activeColor else idleColor
                            val textC = if (state == StepState.ACTIVE) activeColor else idleColor
                            Box(
                                modifier = Modifier
                                    .size(circleDp)
                                    .clip(CircleShape)
                                    .border(2.dp, border, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (index + 1).toString(),
                                    color = textC,
                                    fontWeight = if (state == StepState.ACTIVE) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = numSp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(spaceDp))

        // 라벨들
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = sidePad),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            titles.forEachIndexed { index, title ->
                val color = when {
                    index < completed -> activeColor
                    index == min(completed, titles.lastIndex) && completed != titles.size -> activeColor
                    else -> idleColor
                }

                Box(
                    modifier = Modifier.width(circleDp), // 원과 동일한 폭
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = title,
                        color = color,
                        fontSize = textSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        maxLines = 2,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MakingPreview() {
    MakingBar ()
}


private enum class StepState { IDLE, ACTIVE, DONE }
