package com.app.stronglife.ui.screen.EndScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.stronglife.ui.theme.Stronglife
import com.app.stronglife.ui.theme.cardPayGray
import com.app.stronglife.ui.theme.lightGray
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.superLightGray

@Composable
fun PrevNextBox(isAnswered: Boolean,) {
    val density = LocalDensity.current
    val spaceDp = with(density) {24f.toDp()}

    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        /* Todo : onClick  함수 설정 */
        OneBox(isNext = false, isAnswered = isAnswered, onClick = {})
        Spacer(modifier = Modifier.width(spaceDp))
        OneBox(isNext = true, isAnswered = isAnswered, onClick = {})
    }
}

@Composable
fun OneBox(isNext: Boolean, isAnswered: Boolean, onClick: () -> Unit) {
    val density = LocalDensity.current
    val boxWidth = with(density) { 300f.toDp()}
    val boxHeight = with(density) {89f.toDp()}
    val roundDp = with(density) {12f.toDp()}
    val textSp = with(density) {28f.toSp()}

    Box(
        modifier = Modifier
            .size(boxWidth, boxHeight)
            .background(
                if (isAnswered && isNext) mainRed else superLightGray,
                RoundedCornerShape(roundDp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if(isNext) "다음" else "이전",
            fontFamily = Stronglife,
            fontWeight = FontWeight.Medium,
            fontSize = textSp,
            color = if(isAnswered && isNext) Color.White else lightGray
        )
    }
}

@Preview(device = "spec:width=1920px,height=1080px,dpi=82")
@Composable
fun PrevNextBoxPreview() {
    PrevNextBox(
        isAnswered = true
    )
}