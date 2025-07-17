package com.app.stronglife.ui.screen.PayingScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.R
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.cardPayGray
import com.app.stronglife.ui.theme.midGray
import kotlinx.coroutines.delay

@Composable
fun PayingScreen(navController: NavController) {
    val density = LocalDensity.current
    val widDp = with(density) {1231f.toDp()}
    val heightDp = with(density) {824f.toDp()}
    val roundDp = with(density) {32f.toDp()}
    val textSp = with(density) {36f.toSp()}
    val spaceDp = with(density) {81f.toDp()}
    val space2Dp = with(density) {134f.toDp()}

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        TopBar(4, listOf("섭취시점 선택", "메뉴선택", "주문 확인", "결제하기"), navController)

        Spacer(modifier = Modifier.height(spaceDp))
        Column (
            modifier = Modifier
                .width(widDp)
                .height(heightDp)
                .background(color = Color.White, shape = RoundedCornerShape(roundDp))
                .border(2.dp, cardPayGray, RoundedCornerShape(roundDp))
                .padding(top = space2Dp),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "결제가 진행 중이에요",
                style = TextStyle(
                    fontSize = textSp,
                    fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                    fontWeight = FontWeight.Bold,
                    color = midGray
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(1500)
        navController.navigate("end")
    }
}

@Preview
@Composable
fun PScPreview() {
    PayingScreen(navController = rememberNavController())
}