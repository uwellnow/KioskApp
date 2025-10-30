package com.app.stronglife.ui.screen.HelloScreen

import CartViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.R
import com.app.stronglife.data.remote.PrefsManager
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.util.LanguageManager
import com.app.stronglife.viewmodel.UserCodeViewModel

@Composable
fun HelloScreen(navController: NavController, cartViewModel: CartViewModel,
                userViewModel: UserCodeViewModel, apiKey: String, languageManager: LanguageManager) {
    val context = LocalContext.current
    val prefsManager = remember { PrefsManager(context) }

    val langTag by languageManager.languageTag.collectAsState()

    // apiKey가 없으면 register 화면으로 이동
    LaunchedEffect(apiKey) {
        if (!prefsManager.hasApiKey()) {
            navController.navigate("register") {
                popUpTo("hello") { inclusive = true }
            }
        }
    }

    // 입력 감지 타이머
    LaunchedEffect(Unit) {
        userViewModel.sendApiKey(apiKey) // API Key 전송
        UserCodeViewModel.getInstance(RetrofitClient.api).resetAll()
        cartViewModel.clearCart()
    }

    val density = LocalDensity.current
    val horDp = with(density) {112f.toDp()}
    val verDp = with(density) {169f.toDp()}
    val space1Dp = with(density) {28f.toDp()}
    val space2Dp = with(density) {86f.toDp()}

    val titleSp = with(density) {100f.toSp()}
    val desSp = with(density) {32f.toSp()}

    val padDp = with(density) {44f.toDp()}
    val btnSpaceDp = with(density) {16f.toDp()}


    Box (
        modifier = Modifier.fillMaxSize()
    ){
        Row (
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(padDp),
            horizontalArrangement = Arrangement.spacedBy(btnSpaceDp)
        ){
            LanguageBtn(
                icon = R.drawable.korean,
                lang = "한국어",
                isClick = langTag == "ko",
                onClick = { languageManager.setLanguage("ko", true) }
            )

            LanguageBtn(
                icon = R.drawable.english,
                lang = "English",
                isClick = langTag == "en",
                onClick = { languageManager.setLanguage("en", true) }
            )
        }


        Column(
            modifier = Modifier.fillMaxSize()
                .padding(start = horDp, top = verDp)
        ) {
            Text(
                text = "나에게 딱 맞는\nAI 맞춤형 피트니스 보충제",
                style = TextStyle(
                    fontSize = titleSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            )

            Spacer(modifier = Modifier.height(space1Dp))
            Text(
                text = buildAnnotatedString {
                    append("운동 전/중/후, 나에게 맞는 보충제를 ")

                    withStyle(style = SpanStyle(color = mainRed)) {
                        append("30초 만에")
                    }
                },
                style = TextStyle(
                    fontSize = desSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF939393)
                )
            )
            Spacer(modifier = Modifier.height(space2Dp))

            StartBtn(navController)
        }
    }
}
