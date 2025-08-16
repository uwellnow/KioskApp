package com.app.stronglife.ui.screen.PayScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.app.stronglife.R
import com.app.stronglife.ui.theme.background
import com.app.stronglife.ui.theme.boldGray
import com.app.stronglife.ui.theme.keyGray

@Composable
fun NumberBtn(number: String, onClick: () -> Unit) {

    val density = LocalDensity.current
    val sizeDp = with (density) {96f.toDp()}
    val numberDp = with (density) {60f.toSp()}

    Box(
        modifier = Modifier.size(sizeDp, sizeDp).background(background, shape = CircleShape)
            .clickable{onClick()},
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = number,
            style = TextStyle(
                fontSize = numberDp,
                fontFamily = FontFamily(Font(R.font.pretendard_light)),
                color = keyGray
            ),
        )
    }
}

