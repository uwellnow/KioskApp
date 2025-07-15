package com.app.stronglife.ui.screen.firstScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.sp
import com.app.stronglife.R
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.background

@Composable
fun FirstScreen() {
    val density = LocalDensity.current
    val titleInSp = with(density) {80f.toSp()}
    val paddingInDp = with(density) {80f.toDp()}
    val horpaddingInDp = with(density) {55f.toDp()}
    val contentInSp = with(density) {40f.toSp()}
    val horInDp = with(density) {20f.toDp()}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        TopBar("1", "섭취시점 선택")
        Spacer(modifier = Modifier.width(30.dp))
        Text(
            text = "운동 전-중-후에 필요한\n보충제를 각 단계별로 구매해보세요",
            style = TextStyle(
                fontSize = titleInSp,
                lineHeight = titleInSp * 1.25,
                letterSpacing = (-2).sp,
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
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier.padding(start = paddingInDp, end = paddingInDp),
            horizontalArrangement = Arrangement.spacedBy(horInDp)
        ){
            TimeSelectBtn("운동 전", "각성, 집중력 및 운동 퍼포먼스 향상에\n도움을 줄 수 있습니다", "Pre-\nworkout")
            TimeSelectBtn("운동 중", "운동 지속성 향상, 피로지연\n수분/미네랄/ 에너지 보충에 도움을 줄 수 있습니다", "Intra-\nworkout")
            TimeSelectBtn("운동 후", "근육 회복 및 합성 촉진, 빠른 회복,\n글리코겐 보충에 도움을 줄 수 있습니다", "Post-\nworkout")
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