package com.app.stronglife.ui.screen.CartScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.app.stronglife.R
import com.app.stronglife.ui.theme.lightGray
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.superLightGray

@Composable
fun UnderBtn(navController: NavController) {
    val density = LocalDensity.current
    val backWidDp = with(density) {453f.toDp()}
    val payWidDp = with(density) {1290f.toDp()}
    val heightDp = with(density) {127f.toDp()}
    val backSp = with(density) {40f.toSp()}
    val paySp = with(density) {48f.toSp()}
    val roundDp = with(density) {20f.toDp()}
    val spaceDp = with(density) {38f.toDp()}

    Row (
        modifier = Modifier.fillMaxWidth()
    ){
        Box(
            modifier = Modifier
                .width(backWidDp)
                .height(heightDp)
                .background(superLightGray,
                    shape = RoundedCornerShape(roundDp)
                )
                .clickable{navController.navigate("addOrCart")},
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "뒤로가기",
                style = TextStyle(
                    fontSize = backSp,
                    fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                    fontWeight = FontWeight.Normal,
                    color = lightGray
                )
            )
        }

        Spacer(modifier = Modifier.width(spaceDp))

        Box(
            modifier = Modifier
                .width(payWidDp)
                .height(heightDp)
                .background(
                    mainRed,
                    shape = RoundedCornerShape(roundDp)
                )
                .clickable { navController.navigate("paySelect")},
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "결제하기",
                style = TextStyle(
                    fontSize = paySp,
                    fontFamily = FontFamily(Font(R.font.sfpro_semibold)),
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            )
        }
    }
}
