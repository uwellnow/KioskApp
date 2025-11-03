package com.app.stronglife.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
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
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.errorGray
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.shadowGray


@Composable
fun ErrorBox(errorMsg: String, desMsg: String, onConfirm: () -> Unit) {
    val density = LocalDensity.current
    val widDp = with(density) {178f.toDp()}
    val roundDp = with(density) {32f.toDp()}
    val ErrorSp = with(density) {70f.toSp()}
    val DesSp = with(density) {32f.toSp()}
    val spaceDp = with(density) {56f.toDp()}
    val btnSp = with(density) {28f.toSp()}
    val round2dp = with(density) {48f.toDp()}
    val space2Dp = with(density) {157f.toDp()}



    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(R.drawable.error),
                contentDescription = "에러발생",
                modifier = Modifier.size(widDp)
            )
            Spacer(modifier = Modifier.height(spaceDp))
            Text(
                text = errorMsg,
                style = TextStyle(
                    fontSize = ErrorSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    color = black
                )
            )
            Spacer(modifier = Modifier.height(roundDp))
            Text(
                text = desMsg,
                style = TextStyle(
                    fontSize = DesSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = errorGray
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(space2Dp))
            Box(
                modifier = Modifier
                    .background(color = mainRed, shape = RoundedCornerShape(round2dp))
                    .clickable {
                        onConfirm()
                    },

                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "돌아가기",
                    style = TextStyle(
                        fontSize = btnSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = Color.White
                    ),
                    modifier = Modifier.padding(horizontal = 70.dp, vertical = 30.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ErrorBoxPreview() {
    ErrorBox("서버 점검중", "서버 점검중입니다. 잠시만 기다려 주세요", onConfirm = {})
}