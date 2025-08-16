package com.app.stronglife.ui.screen.firstScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.stronglife.R
import com.app.stronglife.ui.theme.mainRed

@Composable
fun NumberCircleWithText(number: String, title: String, isActive: Boolean, textSizeSp: Float) {
    val alpha = if (isActive) 1f else 0.5f
    val density = LocalDensity.current
    val numberCircleSize = with(density) { 43f.toDp() }
    val numberFontSize = with(density) { 32f.toSp() }
    val textFontSize = with(density) { textSizeSp.toSp() }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(numberCircleSize)
                .background(Color.White.copy(alpha = alpha), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                fontSize = numberFontSize,
                fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                fontWeight = FontWeight.Bold,
                color = mainRed.copy(alpha = alpha)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            fontSize = textFontSize,
            fontFamily = FontFamily(Font(R.font.sfpro_regular)),
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = alpha)
        )
    }
}
