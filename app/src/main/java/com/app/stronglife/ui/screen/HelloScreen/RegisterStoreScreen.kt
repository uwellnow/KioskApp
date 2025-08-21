package com.app.stronglife.ui.screen.HelloScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.R
import com.app.stronglife.data.remote.PrefsManager
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.ui.component.ErrorBox
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.screen.PayScreen.KeyPad
import com.app.stronglife.ui.screen.PayingScreen.MemberErrorBox
import com.app.stronglife.ui.theme.background
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.cardPayGray
import com.app.stronglife.ui.theme.lightGray
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.midGray
import com.app.stronglife.viewmodel.UserCodeViewModel
import com.app.stronglife.viewmodel.UserCodeViewModelFactory

@Composable
fun RegisterStoreScreen(
    navController: NavController,
    userCodeViewModel: UserCodeViewModel,
    onApiKeySet: (String) -> Unit
) {
    val context = LocalContext.current
    val prefsManager = remember { PrefsManager(context) }
    val errorState by userCodeViewModel.errorState

    
    var storeCode by remember { mutableStateOf("") }
    var isFilled by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (prefsManager.hasApiKey()) {
            onApiKeySet(prefsManager.getApiKey())
        }
        focusRequester.requestFocus()
    }

    // 에러 상태에 따른 처리
    when (val error = errorState) {
        is UserCodeViewModel.UiError.None -> {
            // 에러 없음 - 정상 화면 표시
        }
        is UserCodeViewModel.UiError.Generic -> {
            ErrorBox("매장 등록 오류", "발급 받으신 키를 정확히 입력해 주세요") {
                userCodeViewModel.errorState.value = UserCodeViewModel.UiError.None
            }
            return
        }
        is UserCodeViewModel.UiError.Exception -> {
            ErrorBox("연결 오류", "서버 연결에 실패했습니다. 다시 시도해주세요.") {
                userCodeViewModel.errorState.value = UserCodeViewModel.UiError.None
            }
            return
        }
        else -> {
            // 다른 에러 타입은 무시
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val density = LocalDensity.current
    val barbtnSpace = with(density) {81f.toDp()}
    val widDp = with(density) {700f.toDp()}
    val heightDp = with(density) {824f.toDp()}
    val space1dp = with(density) {30f.toDp()}
    val spaceDp = with(density) {98f.toDp()}
    val titleSp = with(density) {70f.toSp()}
    val spacerDp = with(density) {98f.toDp()}
    val descSp = with(density) {36f.toSp()}
    val textSp = with(density) {48f.toSp()}
    val boxWidDp = with(density) {120f.toDp()}
    val boxHeiDp = with(density) {60f.toDp()}
    val boxTextSp = with(density) {32f.toSp()}
    val roundDp = with(density) {8f.toDp()}
    val spacer2Dp = with(density) {24f.toDp()}
    val textwiddp = with(density) {327f.toDp()}

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "헬스장 등록을 위해 발급받으신 QR 코드를 스캔해주세요.",
            style = TextStyle(
                fontSize = titleSp,
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                color = black
            )
        )
        Spacer(modifier = Modifier.height(space1dp))

        Text(
            text = "QR코드 인식이 원활하지 않다면, 직접 숫자로 입력해주세요.",
            style = TextStyle(
                fontSize = descSp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                color = lightGray
            )
        )

        Spacer(modifier = Modifier.height(spaceDp))

        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(widDp).padding(horizontal = 40.dp)
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 바코드 스캐너 및 키패드 입력을 위한 텍스트 필드
                BasicTextField(
                    value = storeCode,
                    onValueChange = { newValue ->
                        // 숫자만 입력 가능하도록 제한
                        if (newValue.all { it.isDigit() } && newValue.length <= 10) {
                            storeCode = newValue
                        }
                    },
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .focusable(),
                    textStyle = TextStyle(
                        fontSize = textSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontWeight = FontWeight.Medium,
                        color = black,
                        textAlign = TextAlign.Center
                    ),


                )
            }

            Spacer(modifier = Modifier.width(spacerDp))

            isFilled = storeCode.isNotEmpty()

            Box(
                modifier = Modifier.background(if (isFilled) mainRed else background, shape = RoundedCornerShape(roundDp))
                    .size(boxWidDp, boxHeiDp)
                    .clickable(enabled = isFilled) {
                        if (isFilled) {
                            userCodeViewModel.sendApiKey(storeCode) { success ->
                                if (success) {
                                    prefsManager.saveApiKey(storeCode)
                                    onApiKeySet(storeCode)
                                }
                                // 실패 시 에러는 errorState를 통해 처리됨
                            }
                        }
                    }
                ,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "확인",
                    style = TextStyle(
                        fontSize = boxTextSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontWeight = FontWeight.Medium,
                        color = if (isFilled) Color.White else midGray
                    )
                )
            }
        }

        Divider(modifier = Modifier.width(widDp).padding(top = spacer2Dp),
            color = lightGray)

        Spacer(modifier = Modifier.height(spacerDp))

        KeyPad(
            onNumberClick = { digit -> 
                storeCode += digit
            },
            onDeleteClick = { 
                if (storeCode.isNotEmpty()) {
                    storeCode = storeCode.dropLast(1)
                }
            },
            onClearClick = {
                storeCode = ""
            }
        )
    }
}
