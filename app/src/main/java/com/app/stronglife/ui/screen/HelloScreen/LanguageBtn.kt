package com.app.stronglife.ui.screen.HelloScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.app.stronglife.R
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.mainRed

@Composable
fun LanguageBtn(icon: Int, lang: String, isClick: Boolean, onClick: () -> Unit) {
    val density = LocalDensity.current
    val widDp = with(density) {161f.toDp()}
    val heiDp = with(density) {60f.toDp()}
    val textSp = with(density) {26f.toSp()}
    val horPadDp = with(density) {20f.toDp()}
    val verPadDp = with(density) {14f.toDp()}
    val roundDp = with(density) {40f.toDp()}
    val spaceDp = with(density) {8f.toDp()}

    val bgColor = if (isClick) mainRed else Color.White
    val textColor = if (isClick) Color.White else black

    Row (
        modifier = Modifier.height(heiDp)
            .background(bgColor, RoundedCornerShape(roundDp))
            .padding(horPadDp, verPadDp)
            .clickable{
                Log.d("LanguageBtn", "Clicked $lang")
                onClick ()},
        horizontalArrangement = Arrangement.spacedBy(spaceDp)

    ){
        Image(
            painter = painterResource(icon),
            contentDescription = "국기",
        )
        Text(
            text = lang,
            fontSize = textSp,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            color = textColor
        )
    }
}