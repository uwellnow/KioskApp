package com.app.stronglife.ui.screen.firstScreen

import CartViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.R
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.black

private val previewCartViewModel = CartViewModel()

@Composable
fun PurposeBox(title: String, desc: String, image: Int) {
    val density = LocalDensity.current

    val boxWidth = with(density) {415f.toDp()}
    val boxHeight = with(density) {428f.toDp()}
    val boxRoundDp = with(density) {50f.toDp()} // padding도 같이
    val boxTitleSp = with(density) {45f.toSp()}
    val boxDescSp = with(density) {32f.toSp()}
    val boxSpaceDp = with(density) {16f.toDp()}
    val imageHeightDp = with(density) {134f.toDp()}

    Column (
        modifier = Modifier.size(boxWidth, boxHeight)
            .background(color = Color.White, shape = RoundedCornerShape(boxRoundDp))
            .padding(boxRoundDp)
    ) {
        Text(
            text = title,
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = boxTitleSp,
            color = Color(0xFF5A5A5A)
        )
        Spacer(modifier = Modifier.height(boxSpaceDp))
        Text(
            text = desc,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = boxDescSp,
            color = Color(0xFFA0A0A0)
        )
        Spacer(modifier = Modifier.height(boxRoundDp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){
            Image(
                painter = painterResource(id = image),
                contentDescription = "섭취 목적 이미지",
                modifier = Modifier.height(imageHeightDp)
            )
        }
    }
}

@Composable
fun PickPurposeScreen(cartViewModel: CartViewModel, navController: NavController) {
    val density = LocalDensity.current
    val firstPadDp = with(density) {113f.toDp()}
    val titleSp = with(density) {64f.toSp()}
    val descSp = with(density) {40f.toSp()}
    val spaceTextDp = with(density) {27f.toDp()}
    val spaceTotalDp = with(density) {68f.toDp()}
    val spaceSecondDp = with(density) {57f.toDp()}
    val spaceWidDp = with(density) {20f.toDp()}


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar(step = 1, listOf("섭취목적 선택"), navController, cartViewModel)

        Column (
            modifier = Modifier.fillMaxSize().padding(vertical = firstPadDp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "섭취 목적을 선택해주세요",
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                fontSize = titleSp,
                color = black
            )
            Spacer(modifier = Modifier.height(spaceTextDp))
            Text(
                text = "섭취 목적에 맞는 최적의 보충제 조합을 제공해드려요",
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = descSp,
                color = Color(0xFF595959)
            )
            Spacer(modifier = Modifier.height(spaceTotalDp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Column {
                    Spacer(modifier = Modifier.height(spaceSecondDp))
                    PurposeBox(
                        "저녁 운동",
                        "운동 퍼포먼스는 그대로,\n숙면까지 자연스럽게",
                        image = R.drawable.pick_night)
                }

                Spacer(modifier = Modifier.width(spaceWidDp))

                PurposeBox(
                    "다이어트",
                    "칼로리 부담은 낮게,\n지속가능한 감량 루틴",
                    image = R.drawable.pick_diet)

                Spacer(modifier = Modifier.width(spaceWidDp))

                Column {
                    Spacer(modifier = Modifier.height(spaceSecondDp))
                    PurposeBox(
                        "근비대",
                        "무게는 더 올리고,\n회복은 더 빠르게",
                        image = R.drawable.pick_muscle)
                }
                Spacer(modifier = Modifier.width(spaceWidDp))

                PurposeBox(
                    "공복 운동",
                    "에너지는 안정적으로,\n집중은 더욱 오래",
                    image = R.drawable.pick_alarm)

            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=1920px,height=1080px,dpi=81")
@Composable
fun PickPurposeScreenPreview() {
    PickPurposeScreen(cartViewModel = previewCartViewModel, navController = rememberNavController())
}