package com.app.stronglife.ui.screen.PaySelectScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.R
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.background
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.cardPayGray
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.superLightGray

@Composable
fun PaySelectScreen(navController: NavController) {
    val density = LocalDensity.current

    val titleSp = with(density) {52f.toSp()}
    val heightDp = with(density) {600f.toDp()}
    val cardWidDp = with(density) {358f.toDp()}
    val membershipDp = with(density) {832f.toDp()}
    val btnSpaceDp = with(density) {27f.toDp()}
    val cardTitleSp = with(density) {40f.toSp()}
    val memberTitleSp = with(density) {60f.toSp()}
    val DesSp = with(density) {24f.toSp()}

    val space1Dp = with(density) {102f.toDp()}
    val space2Dp = with(density) {57f.toDp()}
    val space3Dp = with(density) {32f.toDp()}

    Column (
        modifier = Modifier.background(background),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopBar(4, listOf("섭취시점 선택", "메뉴선택", "주문 확인", "결제하기"), navController)
        Spacer(modifier = Modifier.height(space1Dp))
        Text(
            text = "결제 방법을 선택해주세요",
            style = TextStyle(
                fontSize = titleSp,
                fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                fontWeight = FontWeight.Bold,
                color = black
            )
        )
        Spacer(modifier = Modifier.height((space2Dp)))

        Row {
            Column (
                modifier = Modifier
                    .width(cardWidDp)
                    .height(heightDp)
                    .background(color = Color.White,
                        shape = RoundedCornerShape(space3Dp))
                    .border(2.dp, cardPayGray, RoundedCornerShape(space3Dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "카드 결제",
                    style = TextStyle(
                        fontSize = cardTitleSp,
                        fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                        fontWeight = FontWeight.Bold,
                        color = cardPayGray
                    )
                )
                Spacer(modifier = Modifier.height(space3Dp))

                Text(
                    text = "지금은 멤버십 차감 방식으로만\n결제가 가능해요",
                    style = TextStyle(
                        fontSize = DesSp,
                        fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                        fontWeight = FontWeight.Normal,
                        color = cardPayGray
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.width(btnSpaceDp))

            Column (
                modifier = Modifier
                    .width(membershipDp)
                    .height(heightDp)
                    .background(color = Color.White,
                        shape = RoundedCornerShape(space3Dp))
                    .border(2.dp, color = mainRed, shape = RoundedCornerShape(space3Dp))
                    .clickable{navController.navigate("pay")},
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "멤버십 결제",
                    style = TextStyle(
                        fontSize = memberTitleSp,
                        fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                        fontWeight = FontWeight.Bold,
                        color = mainRed
                    )
                )
                Spacer(modifier = Modifier.height(space3Dp))

                Text(
                    text = "아직 멤버십에 등록하지 않았다면\n카카오톡 채널 ‘유웰나우’에서 자세한 방법을 확인해주세요",
                    style = TextStyle(
                        fontSize = DesSp,
                        fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                        fontWeight = FontWeight.Normal,
                        color = mainRed
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun PSPreview() {
    PaySelectScreen(navController = rememberNavController())
}