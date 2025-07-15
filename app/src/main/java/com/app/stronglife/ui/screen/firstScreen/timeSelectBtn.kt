package com.app.stronglife.ui.screen.firstScreen

import androidx.compose.foundation.background
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.app.stronglife.R
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.lightGray
import com.app.stronglife.ui.theme.lightRed

@Composable
fun TimeSelectBtn(time:String, description:String, english:String) {
    val density = LocalDensity.current
    val widthInDp = with(density) {568.toDp()}
    val heightInDp = with(density) {443.toDp()}
    val roundInDp = with(density) {20f.toDp()}
    val startInDp = with(density) {30f.toDp()}
    val midInDp = with(density) {20f.toDp()}
    val topInDp = with(density) {45f.toDp()}
    val timeInSp = with(density) {36f.toSp()}
    val desInSp = with(density) {20.toSp()}
    val engInSp = with(density) {120.toSp()}

    Box(
        modifier = Modifier
            .width(widthInDp)
            .height(heightInDp)
            .drawBehind {
                drawRoundRect(
                    color = lightRed,
                    cornerRadius = CornerRadius(roundInDp.toPx()),
                    topLeft = Offset(1f, 4f),
                    size = this.size,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 12f), // 테두리 두께
                    alpha = 0.3f
                )
            }
            .background(
                color = Color.White,
                shape = RoundedCornerShape(roundInDp))

        ){
        Column(
            modifier = Modifier
                .padding(start = startInDp, top = topInDp)
        ) {
            Text(
                text = time,
                style = TextStyle(
                    fontSize = timeInSp,
                    fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                    fontWeight = FontWeight.Bold,
                    color = black
                )
            )
            Spacer(modifier = Modifier.height(midInDp))
            Text(
                text = description,
                style = TextStyle(
                    fontSize = desInSp,
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
                    letterSpacing = (-3).sp,
                    fontFamily = FontFamily(Font(R.font.sfpro_black)),
                    fontWeight = FontWeight.Black,
                    color = lightRed,

                    )
            )
        }
    }

}

@Composable
@Preview
fun TimeSelectBtnPreview() {
    TimeSelectBtn("운동 전", "각성, 집중력 및 운동 퍼포먼스 향상에\n도움을 줄 수 있습니다", "Pre-\nworkout")
}
