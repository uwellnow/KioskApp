package com.app.stronglife.ui.screen.PayScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.stronglife.ui.theme.cardPayGray

@Composable
fun PayOverlayCard() {
    val density = LocalDensity.current
    val widDp = with(density) {1231f.toDp()}
    val heightDp = with(density) {824f.toDp()}
    val roundDp = with(density) {32f.toDp()}

    var selected by remember { mutableStateOf("QR") }

    Column (
        modifier = Modifier
            .width(widDp)
            .height(heightDp)
            .background(color = Color.White, shape = RoundedCornerShape(roundDp))
            .border(2.dp, cardPayGray, RoundedCornerShape(roundDp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row (
            modifier = Modifier.fillMaxWidth()
        ){
            PaymentTab(
                text = "QR코드 스캔",
                isSelected = selected == "QR",
                onClick = { selected = "QR"},
            )
            PaymentTab(
                text = "휴대전화로 조회",
                isSelected = selected == "phone",
                onClick = { selected = "phone"},
            )


        }
    }
}

@Preview
@Composable
fun POPreview() {
    PayOverlayCard()
}