package com.app.stronglife.ui.screen.EndScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.stronglife.R
import com.app.stronglife.ui.theme.Stronglife
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.mainRed

@Composable
fun ChoiceItem(choice: String, isSelected: Boolean, onClick: () -> Unit) {
    val density = LocalDensity.current
    val barWidth = with(density) {685f.toDp()}
    val barHeight = with(density) {112f.toDp()}
    val textSp = with(density) {30f.toSp()}
    val spaceDp = with(density) {24f.toDp()}
    val circleSize = with(density) {40f.toDp()}

    val borderDp = with(density) {4f.toDp()}

    Row(
        modifier = Modifier.size(barWidth,barHeight)
            .border(borderDp, color = if(isSelected) mainRed else Color(0xFFE5E7EB), shape = RoundedCornerShape(16.dp))
            .background(color = if(isSelected) Color(0xFFFDF7F8) else Color.White, RoundedCornerShape(16.dp))
            .clickable {onClick()},
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(circleSize))

        Image(
            painter = if(isSelected) painterResource(id = R.drawable.chosen_radio_btn) else painterResource(id = R.drawable.radio_btn),
            contentDescription = "선택 버튼",
            modifier = Modifier.size(circleSize)
        )

        Spacer(modifier = Modifier.width(spaceDp))

        Text(
            text = choice,
            fontFamily = Stronglife,
            fontWeight = FontWeight.Medium,
            fontSize = textSp,
            color = if(isSelected) mainRed else black
        )

    }
}

@Preview( device = "spec:width=1920px,height=1080px,dpi=82")
@Composable
fun ChoiceItemPreview() {
    ChoiceItem(choice = "자영업/프리랜서", isSelected = true, onClick = {})
}