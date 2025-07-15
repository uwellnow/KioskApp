package com.app.stronglife.ui.screen.menuScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.app.stronglife.R
import com.app.stronglife.ui.theme.mainRed

@Composable
fun TimeCategory(time:String) {
    val density = LocalDensity.current
    val widthtoDp = with(density) {171f.toDp()}
    val heighttoDp = with(density) {50f.toDp()}
    val roundtoDp = with(density) {20f.toDp()}
    val texttoSp = with(density) {24f.toSp()}

    Box (
       modifier = Modifier
           .width(widthtoDp)
           .height(heighttoDp)
           .background(
               color = mainRed,
               shape = RoundedCornerShape(topStart = roundtoDp, topEnd = roundtoDp)
           ),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = time,
            style = TextStyle(
                fontSize = texttoSp,
                fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                fontWeight = FontWeight.Bold,
                color = Color.White ))

                }


}

@Preview
@Composable
fun TimeCategoryPreview() {
    TimeCategory("운동 전")
}