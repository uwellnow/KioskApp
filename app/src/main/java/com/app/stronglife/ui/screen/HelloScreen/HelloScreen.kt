package com.app.stronglife.ui.screen.HelloScreen

import CartViewModel
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.stronglife.R
import com.app.stronglife.data.remote.PrefsManager
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.viewmodel.UserCodeViewModel

@Composable
fun HelloScreen(navController: NavController, cartViewModel: CartViewModel, userViewModel: UserCodeViewModel, apiKey: String) {
    val context = LocalContext.current
    val prefsManager = remember { PrefsManager(context) }
    
    // apiKey가 없으면 register 화면으로 이동
    LaunchedEffect(apiKey) {
        if (!prefsManager.hasApiKey()) {
            navController.navigate("register") {
                popUpTo("hello") { inclusive = true }
            }
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize().clickable {navController.navigate("first")},
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        // 입력 감지 타이머
        LaunchedEffect(Unit) {
            userViewModel.sendApiKey(apiKey) // API Key 전송
            if (cartViewModel.cartItems.value.isNotEmpty()) {
                cartViewModel.clearCart()
            }
        }

        val density = LocalDensity.current
        val widDp = with(density) {494f.toDp()}
        val heiDp = with(density) {294f.toDp()}
        val textSp = with(density) {36f.toSp()}
        val spaceDp = with(density) {100f.toDp()}

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    navController.navigate("first")
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.hello),
                contentDescription = "처음 보이는 화면",
                modifier = Modifier
                    .width(widDp)
                    .height(heiDp)
            )

            Spacer(modifier = Modifier.height(spaceDp))

            Text(
                text = "화면을 터치하여 주문을 시작하세요",
                style = TextStyle(
                    fontSize = textSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontWeight = FontWeight.Medium,
                    color = mainRed,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}
