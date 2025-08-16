package com.app.stronglife.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.app.stronglife.R
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.boldGray

@Composable
fun CartAlertBox(number: Number, modifier: Modifier = Modifier) {

    val density = LocalDensity.current
    val circleDp = with(density) {16f.toDp()}
    val textInSp = with(density) {12f.toSp()}

    Box(
        modifier = modifier
            .size(circleDp)
            .background(black, CircleShape),
        contentAlignment = Alignment.Center

    ) {
        Text(
            text = number.toString(),
            style = TextStyle(
                fontSize = textInSp,
                fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                color = Color.White
            ),
        )
    }
}

@Preview()
@Composable
fun CartPreview() {
    CartAlertBox(2)
}