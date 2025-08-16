package com.app.stronglife.ui.screen.firstScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.stronglife.R
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.background
import com.app.stronglife.ui.theme.boldGray
import com.app.stronglife.ui.theme.descGray

@Composable
fun FirstScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel()
) {
    val density = LocalDensity.current
    val titleInSp = with(density) {70f.toSp()}
    val paddingInDp = with(density) {80f.toDp()}
    val horpaddingInDp = with(density) {102f.toDp()}
    val contentInSp = with(density) {36f.toSp()}
    val horInDp = with(density) {16f.toDp()}
    val spaceDp = with (density) {28f.toDp()}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        TopBar(step = 1, pageNames = listOf("섭취시점 선택"), navController = navController, cartViewModel = cartViewModel)
        Text(
            text = "언제 드실 보충제인가요?",
            style = TextStyle(
                fontSize = titleInSp,
                lineHeight = titleInSp * 1.25,
                letterSpacing = (-2).sp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontWeight = FontWeight.Bold,
                color = boldGray
            ),
            modifier = Modifier.padding(start = paddingInDp, top = horpaddingInDp)
        )
        Spacer(modifier = Modifier.height(horInDp))
        Text(
            text = "운동 각 단계에 필요한 보충제를 섭취해 보세요",
            style = TextStyle(
                fontSize = contentInSp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontWeight = FontWeight.Normal,
                color = descGray
            ),
            modifier = Modifier.padding(start = paddingInDp)
        )
        Spacer(modifier = Modifier.height(horInDp))
        Row(
            modifier = Modifier.padding(start = paddingInDp, top = paddingInDp),
            horizontalArrangement = Arrangement.spacedBy(spaceDp)
        ){
            TimeSelectBtn("운동 전", "각성, 집중력 및 운동 퍼포먼스 향상에\n도움을 줄 수 있습니다", "Pre-\nworkout", navController)
            TimeSelectBtn("운동 중", "운동 지속성 향상, 피로지연\n수분/미네랄/ 에너지 보충에 도움을 줄 수 있습니다", "Intra-\nworkout", navController)
            TimeSelectBtn("운동 후", "근육 회복 및 합성 촉진, 빠른 회복,\n글리코겐 보충에 도움을 줄 수 있습니다", "Post-\nworkout", navController)
        }
    }
}

@Preview(showBackground = true,
    device = "spec:width=1920px,height=1080px,dpi=82")
@Composable
fun FirstPreview() {
    FirstScreen(navController = rememberNavController())
}
