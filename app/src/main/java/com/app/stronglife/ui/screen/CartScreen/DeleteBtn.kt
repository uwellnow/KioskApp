package com.app.stronglife.ui.screen.CartScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import com.app.stronglife.R
import com.app.stronglife.ui.theme.lightGray

@Composable
fun DeleteBtn() {
    val density = LocalDensity.current
    val widthtoDp = with(density) {113f.toDp()}
    val heighttoDp = with(density) {58f.toDp()}
    val textSp = with(density) {32f.toSp()}
    val roundDp = with (density) {12f.toDp()}

    Box (
        modifier = Modifier
            .width(widthtoDp)
            .height(heighttoDp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(roundDp)
            )
            .border(2.dp, lightGray, shape = RoundedCornerShape(roundDp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "삭제",
            style = TextStyle(
                fontSize = textSp,
                fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        )
    }
}

@Preview
@Composable
fun DeleteBtnPreview() {
    DeleteBtn()
}