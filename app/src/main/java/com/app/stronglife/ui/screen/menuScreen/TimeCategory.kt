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
import com.app.stronglife.R
import com.app.stronglife.ui.theme.mainRed

@Composable
fun TimeCategory(time:String, modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    val widthtoDp = with(density) {96f.toDp()}
    val heighttoDp = with(density) {46f.toDp()}
    val roundtoDp = with(density) {12f.toDp()}
    val texttoSp = with(density) {24f.toSp()}

    Box (
       modifier = modifier
           .width(widthtoDp)
           .height(heighttoDp)
           .background(
               color = mainRed,
               shape = RoundedCornerShape(roundtoDp)
           ),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = time,
            style = TextStyle(
                fontSize = texttoSp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                color = Color.White ))

                }


}

