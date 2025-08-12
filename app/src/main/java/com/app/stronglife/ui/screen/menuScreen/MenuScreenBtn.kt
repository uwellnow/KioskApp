package com.app.stronglife.ui.screen.menuScreen

import android.view.RoundedCorner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.app.stronglife.R
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.superLightGray

@Composable
fun MenuScreenBtn(
    onBackClick: () -> Unit = {},
    onCartClick: () -> Unit = {}
) {
    val density = LocalDensity.current
    val roundtoDp = with(density) { 12f.toDp() }
    val texttoSp = with(density) {28f.toSp()}
    val heighttoDp = with(density) {105f.toDp()}
    val backwidtoDp = with(density) {408f.toDp()}
    val cartwidtoDp = with(density) {1161f.toDp()}
    val btnSpacetoDp = with(density) {16f.toDp()}

    Row {
        Box(
            modifier = Modifier
                .width(backwidtoDp)
                .height(heighttoDp)
                .background(
                    color = superLightGray,
                    shape = RoundedCornerShape(roundtoDp)
                )
                .clickable{ onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "뒤로가기",
                style = TextStyle(
                    fontSize = texttoSp,
                    fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                    fontWeight = FontWeight.Normal,
                    color = black
                )
            )
        }

        Spacer(modifier = Modifier.width(btnSpacetoDp))

        Box(
            modifier = Modifier
                .width(cartwidtoDp)
                .height(heighttoDp)
                .background(
                    color = mainRed,
                    shape = RoundedCornerShape(roundtoDp)
                )
                .clickable{ onCartClick()},
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "장바구니에 담기",
                style = TextStyle(
                    fontSize = texttoSp,
                    fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}
