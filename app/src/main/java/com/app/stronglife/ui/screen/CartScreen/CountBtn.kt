package com.app.stronglife.ui.screen.CartScreen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.app.stronglife.R
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.lightGray

@Composable
fun CountBtn(
    count: Int,
    isOne: Boolean,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    val density = LocalDensity.current
    val heightDp = with(density) {57f.toDp()}
    val countToSp = with(density) {48f.toSp()}
    val countSectionWidth = with(density) {248f.toDp()}
    val paddingdp = with(density) {16f.toDp()}

    Row(
        modifier = Modifier
            .width(countSectionWidth),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(heightDp)
                .background(
                    color = Color.White.copy(alpha = if (isOne) 0.4f else 1f),
                    shape = CircleShape
                )
                .border(
                    1.dp,
                    color = lightGray.copy(alpha = if (isOne) 0.5f else 1f),
                    shape = CircleShape
                )
                .alpha(if (isOne) 0.4f else 1f)
                .clickable(enabled = !isOne) { onDecrease() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.minus),
                contentDescription = "감소",
                modifier = Modifier.size(30.dp)
            )
        }

        Text(
            text = count.toString(),
            style = TextStyle(
                fontSize = countToSp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontWeight = FontWeight.Bold,
                color = black
            )

        )

        Box(
            modifier = Modifier
                .size(heightDp)
                .border(1.dp, lightGray, CircleShape)
                .clickable { onIncrease() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "증가",
                tint = lightGray,
                modifier = Modifier.size(45.dp)
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun CountBtnPreview() {
    CountBtn(
        count = 2,
        isOne = false,
        onIncrease = {},
        onDecrease = {}
    )
}
