package com.app.stronglife.ui.screen.firstScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.app.stronglife.ui.component.TopBar

@Composable
fun FirstScreen() {
    val density = LocalDensity.current
    val titleInSp = with(density) {80f.toSp()}
    val paddingInDp = with(density) {80f.toDp()}
    val horpaddingInDp = with(density) {55f.toDp()}
    val contentInSp = with(density) {40f.toSp()}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar("1", "섭취시점 선택")
        Spacer(modifier = Modifier.width(30.dp))
        Text(
            text = "운동 전-중-후에 필요한\n보충제를 각 단계별로 구매해보세요",
            style = TextStyle(
                fontSize = titleInSp,
                fontFamily = FontFamily(Font(R.font.sfpro_semibold)),
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            ),
            modifier = Modifier.padding(start = paddingInDp, top = horpaddingInDp)
        )
        Text(
            text = "언제 드실 보충제인가요?",
            style = TextStyle(
                fontSize = contentInSp,
                fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                fontWeight = FontWeight.Normal,
                color = Color.Black
            ),
            modifier = Modifier.padding(start = paddingInDp, top = horpaddingInDp)
        )
        Spacer(modifier = Modifier.width(15.dp))
        Row(
            modifier = Modifier.padding(horizontal = paddingInDp)
        ){

        }
    }
}

@Composable
@Preview(
    name = "1920x1080 Landscape",
    showBackground = true,
    device = "spec:width=1920px,height=1080px,dpi=81"
)
fun firstScreenPreview() {
    FirstScreen()
}