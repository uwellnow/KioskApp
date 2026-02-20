package com.app.stronglife.ui.screen.EndScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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

// ChoiceQuestion용: 왼쪽에 이미지, 텍스트
@Composable
fun ChoiceItemWithImage(
    choice: String,
    isSelected: Boolean,
    imageResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val barHeight = with(density) {125f.toDp()}
    val textSp = with(density) {30f.toSp()}
    val spaceDp = with(density) {24f.toDp()}
    val imageSize = with(density) {40f.toDp()}
    val borderDp = with(density) {2f.toDp()}
    val paddingDp = with(density) {44f.toDp()}

    Row(
        modifier = modifier
            .height(barHeight)
            .border(borderDp, color = if(isSelected) mainRed else Color(0xFFE5E7EB), shape = RoundedCornerShape(16.dp))
            .background(color = if(isSelected) Color(0xFFFDF7F8) else Color.White, RoundedCornerShape(16.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = paddingDp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 왼쪽에 이미지
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "선택지 이미지",
            modifier = Modifier.size(imageSize)
        )

        Spacer(modifier = Modifier.width(spaceDp))

        // 텍스트
        Text(
            text = choice,
            fontFamily = Stronglife,
            fontWeight = FontWeight.Medium,
            fontSize = textSp,
            color = if(isSelected) mainRed else black
        )
    }
}

// CheckQuestion용: 텍스트, 오른쪽에 체크 아이콘
@Composable
fun CheckItem(
    choice: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val density = LocalDensity.current
    val barWidth = with(density) {631f.toDp()}
    val barHeight = with(density) {125f.toDp()}
    val textSp = with(density) {30f.toSp()}
    val spaceDp = with(density) {24f.toDp()}
    val checkSize = with(density) {40f.toDp()}
    val borderDp = with(density) {2f.toDp()}
    val paddingDp = with(density) {44f.toDp()}

    Row(
        modifier = Modifier.size(barWidth, barHeight)
            .border(borderDp, color = if(isSelected) mainRed else Color(0xFFE5E7EB), shape = RoundedCornerShape(16.dp))
            .background(color = if(isSelected) Color(0xFFFDF7F8) else Color.White, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = paddingDp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 텍스트
        Text(
            text = choice,
            fontFamily = Stronglife,
            fontWeight = FontWeight.Medium,
            fontSize = textSp,
            color = if(isSelected) mainRed else black
        )

        Spacer(modifier = Modifier.weight(1f))

        // 오른쪽에 체크 아이콘
        Image(
            painter = painterResource(
                id = if(isSelected) R.drawable.is_checked else R.drawable.is_not_checked
            ),
            contentDescription = if(isSelected) "선택됨" else "선택 안됨",
            modifier = Modifier.size(checkSize)
        )
    }
}

@Composable
fun ChoiceItem(choice: String, isSelected: Boolean, onClick: () -> Unit) {
    val density = LocalDensity.current
    val barWidth = with(density) {685f.toDp()}
    val barHeight = with(density) {112f.toDp()}
    val textSp = with(density) {30f.toSp()}
    val spaceDp = with(density) {24f.toDp()}
    val circleSize = with(density) {40f.toDp()}

    val borderDp = with(density) {2f.toDp()}

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
    CheckItem(choice = "자영업/프리랜서", isSelected = false, onClick = {})
}