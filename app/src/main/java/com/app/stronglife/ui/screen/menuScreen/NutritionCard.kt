package com.app.stronglife.ui.screen.menuScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.stronglife.R
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.blue
import com.app.stronglife.ui.theme.descGray
import com.app.stronglife.ui.theme.lightRed
import com.app.stronglife.ui.theme.superLightGray
import com.app.stronglife.util.NumberWithComma
import getNutrientMeta


@Composable
fun NutrientCard(nutrition: Nutrition) {
    val meta = getNutrientMeta(nutrition.name)
    val density = LocalDensity.current
    val horWidth = with(density) { 190f.toDp()}
    val verWidth = with(density) { 204f.toDp()}
    val iconSize = with(density) { 26f.toDp()}
    val typeText = with(density) {16f.toSp()}
    val unitText = with(density) {30f.toSp()}
    val nutText = with(density) {18f.toSp()}
    val descText = with(density) {14f.toSp()}
    val spaceDp = with(density) { 24f.toDp()}
    val space2Dp = with(density) { 8f.toDp()}
    val space3Dp = with(density) {20f.toDp()}

    Column(
        modifier = Modifier
            .size(horWidth, verWidth)
            .background(Color.White, RoundedCornerShape(18.dp))
            .border(2.dp, color = lightRed, shape = RoundedCornerShape(18.dp))
            .padding(spaceDp, spaceDp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start

    ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space2Dp)
            ) {
                meta?.let {
                    // 아이콘
                    Image(
                        painter = painterResource(id = it.icon),
                        contentDescription = it.type.displayName,
                        modifier = Modifier.size(iconSize)
                    )

                    // 타입 이름
                    Text(
                        text = it.type.displayName,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = typeText
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(space3Dp))

        val showValue = try {
            nutrition.value.toDouble() != 0.0
        } catch (e: NumberFormatException) {
            true
        }

        if (showValue) {
            Text(
                text = "${NumberWithComma(nutrition.value)}${nutrition.unit}",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    fontWeight = FontWeight.Bold,
                    fontSize = unitText,
                    color = black
                )
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

            Text(
                text = nutrition.name,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = nutText,
                    color = descGray
                )
            )

            Spacer(modifier = Modifier.height(spaceDp))

            // 영양소 설명
            meta?.let {
                Text(
                    text = it.description,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontWeight = FontWeight.Normal,
                        fontSize = descText,
                        color = blue
                    ),
                )
            }
    }
}

