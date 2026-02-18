package com.app.stronglife.ui.screen.EndScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.app.stronglife.R
import com.app.stronglife.ui.theme.mainRed
import kotlinx.coroutines.delay

@Composable
fun DrinkStageBarV2(
    modifier: Modifier = Modifier,
    stepSeconds: Int = 20,
    activeColor: Color = mainRed,
    idleColor: Color = Color(0xFFAFAFAF),
    resetKey: Any? = null,
    onFinished: (() -> Unit)? = null,
    dotsNoRes: Int = R.drawable.dots_no,
    dotsYesRes: Int = R.drawable.dots_yes,
    checkedCircleRes: Int = R.drawable.stage_circle_checked,
) {
    val titles = listOf(
        stringResource(R.string.make_1),
        stringResource(R.string.make_2),
        stringResource(R.string.make_3),
        stringResource(R.string.make_4),
    )

    var stage by remember { mutableIntStateOf(1) }
    LaunchedEffect(resetKey, stepSeconds) {
        stage = 1
        repeat(3) {
            delay(stepSeconds * 1000L)
            stage += 1
        }
        onFinished?.invoke()
    }

    val density = LocalDensity.current
    val circleDp = with(density) { 42f.toDp() }
    val numSp = with(density) { 24f.toSp() }
    val labelSp = with(density) { 24f.toSp() }
    val labelTopSpace = with(density) { 5f.toDp() }
    val dotsSpace = with(density) { 10f.toDp() }
    val dotsWidth = with(density) { 20f.toDp() }

    val activeCircleIndex = when (stage) {
        1 -> 1
        2 -> 2
        else -> 3
    }

    var barSize by remember { mutableStateOf(IntSize(0, 0)) }
    val barWidthDp = with(density) { barSize.width.toDp() }
    val barHeightDp = with(density) { barSize.height.toDp() }
    val descHeightDp = with(density) {52f.toDp()}
    val descBottomSpace = with(density) {13f.toDp()}
    val circlePitch = circleDp + (dotsSpace * 2) + dotsWidth
    val x = when (activeCircleIndex) {
        1 -> 0.dp
        2 -> circlePitch
        else -> circlePitch * 2
    }

    val labelHeightDp = labelSp.value.dp * 1.4f // 대충 한 줄 높이(폰트마다 약간 다름)
    val containerHeight = if (barSize.height > 0) barHeightDp + labelTopSpace + labelHeightDp + descHeightDp + (descBottomSpace * 2) else Dp.Unspecified


    val columnWidth = barWidthDp.takeIf { it > 0.dp }?.let { (it * 1.8f).coerceAtLeast(320.dp) } ?: 320.dp
    Column (
        modifier = modifier
            .width(columnWidth)
            .height(containerHeight)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.drink_stage_desc),
                contentDescription = "음료 제조 과정 말풍선",
                modifier = Modifier.height(descHeightDp)
            )
        }
        Spacer(modifier = Modifier.height(descBottomSpace))
        Row(
            modifier = Modifier
                .onSizeChanged { barSize = it },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            StageCircle(1, stage, circleDp, numSp, activeColor, idleColor, checkedCircleRes)
            Spacer(Modifier.width(dotsSpace))
            DotsTriplet(stage >= 2, dotsNoRes, dotsYesRes, dotsWidth)
            Spacer(Modifier.width(dotsSpace))
            StageCircle(2, stage, circleDp, numSp, activeColor, idleColor, checkedCircleRes)
            Spacer(Modifier.width(dotsSpace))
            DotsTriplet(stage >= 3, dotsNoRes, dotsYesRes, dotsWidth)
            Spacer(Modifier.width(dotsSpace))
            StageCircle(3, stage, circleDp, numSp, activeColor, idleColor, checkedCircleRes)
        }

        if (barSize.width > 0) {
            Text(
                text = titles[stage - 1],
                color = activeColor,
                fontSize = labelSp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Start,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .offset(x = x, y = labelTopSpace)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun DotsTriplet(
    yes: Boolean,
    dotsNoRes: Int,
    dotsYesRes: Int,
    dotsWidth: Dp
) {
    Image(
        painter = painterResource(if (yes) dotsYesRes else dotsNoRes),
        contentDescription = null,
        modifier = Modifier.width(dotsWidth)
    )
}


@Composable
private fun StageCircle(
    index: Int,
    stage: Int, // 1..4
    circleDp: Dp,
    numSp: androidx.compose.ui.unit.TextUnit,
    activeColor: Color,
    idleColor: Color,
    checkedCircleRes: Int,
    modifier: Modifier = Modifier
) {
    val isDone = when (index) {
        1 -> stage >= 2
        2 -> stage >= 3
        3 -> stage >= 4
        else -> false
    }
    val isActive = when (index) {
        1 -> stage == 1
        2 -> stage == 2
        3 -> stage == 3
        else -> false
    }

    when {
        isDone -> {
            Image(
                painter = painterResource(checkedCircleRes),
                contentDescription = "done",
                modifier = modifier.size(circleDp)
            )
        }
        else -> {
            val border = if (isActive) activeColor else idleColor
            val textC = if (isActive) Color.White else idleColor
            val bg = if (isActive) activeColor else Color.Transparent

            Box(
                modifier = modifier
                    .size(circleDp)
                    .clip(CircleShape)
                    .background(bg)
                    .border((0.5).dp, border, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = index.toString(),
                    color = textC,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    fontSize = numSp
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DrinkStageBarV2Preview() {
    DrinkStageBarV2(
        stepSeconds = 20,
        resetKey = 0
    )
}
