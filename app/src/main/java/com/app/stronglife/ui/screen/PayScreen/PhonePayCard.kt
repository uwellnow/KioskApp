package com.app.stronglife.ui.screen.PayScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsProperties.InputText
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.stronglife.R
import com.app.stronglife.ui.theme.background
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.boldGray
import com.app.stronglife.ui.theme.lightGray
import com.app.stronglife.ui.theme.midGray
import java.nio.file.WatchEvent
import kotlin.math.round

@Composable
fun PhonePayCard () {

    val density = LocalDensity.current
    val widDp = with(density) {584f.toDp()}
    val textSp = with(density) {36f.toSp()}
    val roundDp = with(density) {12f.toDp()}
    val boxWidDp = with(density) {97f.toDp()}
    val boxHeiDp = with(density) {44f.toDp()}
    val boxTextSp = with(density) {24f.toSp()}
    val spacerDp = with(density) {60f.toDp()}
    val spacer2Dp = with(density) {17f.toDp()}

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 2.dp)
        ){
            Text(
                text = "휴대폰 번호를 입력해주세요",
                style = TextStyle(
                    fontSize = textSp,
                    fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                    fontWeight = FontWeight.Medium,
                    color = lightGray
                )
            )

            Spacer(modifier = Modifier.width(spacerDp))

            Box(
                modifier = Modifier.background(color = background, shape = RoundedCornerShape(roundDp))
                    .size(boxWidDp, boxHeiDp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "조회",
                    style = TextStyle(
                        fontSize = boxTextSp,
                        fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                        fontWeight = FontWeight.Medium,
                        color = midGray
                    )
                )
            }
        }

        Divider(modifier = Modifier.width(widDp).padding(top = spacer2Dp),
            color = lightGray)

        Spacer(modifier = Modifier.height(spacerDp))

        KeyPad()


    }

}

@Preview(showBackground = true)
@Composable
fun PhonePreview() {
    PhonePayCard()
}