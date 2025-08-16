package com.app.stronglife.ui.screen.menuScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.app.stronglife.R
import com.app.stronglife.ui.theme.mainRed

@Composable
fun SoldOutBox() {
    val density = LocalDensity.current
    val widDp = with (density) {325f.toDp()}
    val heiDp = with(density) {66f.toDp()}
    val roundDp = with(density) {4f.toDp()}
    val textSp = with (density) {40f.toSp()}

    Box(
       modifier = Modifier.size(widDp, heiDp)
           .graphicsLayer(rotationZ = -7f)
           .background(mainRed, RoundedCornerShape(roundDp))
           .graphicsLayer(rotationZ = -7f),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Sold Out",
            style = TextStyle(
                fontSize = textSp,
                letterSpacing = (-2).sp,
                fontFamily = FontFamily(Font(R.font.pretendard_black)),
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        )
    }
}

@Preview(device = "spec:width=1920px,height=1080px,dpi=81")
@Composable
fun SoldOutPreview() {
    SoldOutBox()
}