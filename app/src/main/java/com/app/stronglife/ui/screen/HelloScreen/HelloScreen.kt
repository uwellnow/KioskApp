package com.app.stronglife.ui.screen.HelloScreen

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.app.stronglife.R
import com.app.stronglife.ui.screen.firstScreen.FirstScreen
import com.app.stronglife.ui.theme.lightRed
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.viewmodel.UserCodeViewModel

@Composable
fun HelloScreen(navController: NavController, userViewModel: UserCodeViewModel, apiKey: String) {
    Column (
        modifier = Modifier
            .fillMaxSize().clickable {navController.navigate("first")},
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        LaunchedEffect(Unit) {
            // HelloScreen 진입 시 API 키 전송
            userViewModel.sendApiKey(apiKey)
        }

        val density = LocalDensity.current
        val widDp = with(density) {494f.toDp()}
        val heiDp = with(density) {294f.toDp()}
        val textSp = with(density) {36f.toSp()}
        val spaceDp = with(density) {100f.toDp()}

        val infiniteTransition = rememberInfiniteTransition(label = "")
        val textAlpha by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 0.3f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000),
                repeatMode = RepeatMode.Reverse
            ),
            label = ""
        )

        AsyncImage(
            model = R.drawable.hello,
            contentDescription = "처음 보이는 화면",
            modifier = Modifier.width(widDp).height(heiDp)
                .alpha(textAlpha)
        )

        Spacer(modifier = Modifier.height(spaceDp))

        Text(
            text = "화면을 터치하여 주문을 시작하세요",
            modifier = Modifier.alpha(textAlpha),
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
