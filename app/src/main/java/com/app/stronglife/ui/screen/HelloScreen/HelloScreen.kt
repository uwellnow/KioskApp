package com.app.stronglife.ui.screen.HelloScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.app.stronglife.R
import com.app.stronglife.ui.theme.mainRed

@Composable
fun HelloScreen(navController: NavController) {
    Column (
        modifier = Modifier.clickable {navController.navigate("first")},
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        val density = LocalDensity.current
        val widDp = with(density) {494f.toDp()}
        val heiDp = with(density) {294f.toDp()}
        val textSp = with(density) {36f.toSp()}
        val spaceDp = with(density) {100f.toDp()}

        AsyncImage(
            model = R.drawable.hello,
            contentDescription = "처음 보이는 화면",
            modifier = Modifier.width(widDp).height(heiDp)
        )

        Spacer(modifier = Modifier.height(spaceDp))

        Text(
            text = "화면을 터치하여 주문을 시작하세요",
            style = TextStyle(
                fontSize = textSp,
                fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                fontWeight = FontWeight.Medium,
                color = mainRed,
                textAlign = TextAlign.Center
            )
        )
    }
}
