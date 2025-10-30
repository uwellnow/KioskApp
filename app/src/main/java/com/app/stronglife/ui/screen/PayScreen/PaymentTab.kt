package com.app.stronglife.ui.screen.PayScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.stronglife.R
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.paySelectGray

@Composable
fun PaymentTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {

    val density = LocalDensity.current
    val spaceDp = with(density) {40f.toDp()}
    val widDp = with(density) {614f.toDp()}
    val textSp = with(density) {36f.toSp()}

    val textColor = if (isSelected) mainRed else paySelectGray
    val dividerColor = if (isSelected) mainRed else paySelectGray

    Column (
        modifier = Modifier
            .width(widDp)
            .clickable(onClick = onClick)
            .padding(top = spaceDp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = text,
            color = textColor,
            fontSize = textSp,
            fontFamily = FontFamily(Font(R.font.pretendard_regular))
        )
        Spacer(modifier = Modifier.height(spaceDp))
        Divider(
            color = dividerColor,
            thickness = 2.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentTabPreview() {
    PaymentTab("QR코드 스캔", false, onClick = {})
}
