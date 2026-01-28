package com.app.stronglife.ui.screen.PayScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.DisposableEffect
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.R
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.component.ErrorBox
import com.app.stronglife.ui.screen.PayingScreen.MemberErrorBox
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.cardPayGray
import com.app.stronglife.ui.theme.lightGray
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.viewmodel.UserCodeViewModel
import com.app.stronglife.viewmodel.UserCodeViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun OrderNumScreen(
    navController: NavController, 
    apiKey: String,
    cartViewModel: CartViewModel = viewModel(),
    userCodeViewModel: UserCodeViewModel = UserCodeViewModel.getInstance(RetrofitClient.api)
) {
    val errorState by userCodeViewModel.errorState
    var showError by remember { mutableStateOf(false) }
    var currentError by remember { mutableStateOf<UserCodeViewModel.UiError>(UserCodeViewModel.UiError.None) }

    // 화면을 떠날 때 에러 상태만 초기화 (주문번호는 유지)
    DisposableEffect(Unit) {
        onDispose {
            userCodeViewModel.errorState.value = UserCodeViewModel.UiError.None
            // userCodeViewModel.clear() 제거 - 주문번호 유지
            showError = false
            currentError = UserCodeViewModel.UiError.None
        }
    }

    // 에러 상태 로깅 및 로컬 상태 업데이트
    LaunchedEffect(errorState) {
        Log.d("OrderNumScreen", "Error state changed: $errorState")
        if (errorState !is UserCodeViewModel.UiError.None) {
            currentError = errorState
            showError = true
        }
    }

    // 에러 상태에 따른 처리
    if (showError) {
        when (val error = currentError) {
            is UserCodeViewModel.UiError.None -> {
                Log.d("OrderNumScreen", "No error state")
            }
            is UserCodeViewModel.UiError.NotFound -> {
                Log.d("OrderNumScreen", "Showing NotFound error")
                MemberErrorBox(onConfirm = {
                    userCodeViewModel.errorState.value = UserCodeViewModel.UiError.None
                    showError = false
                    currentError = UserCodeViewModel.UiError.None
                })
                return
            }
            is UserCodeViewModel.UiError.InsufficientBalance -> {
                Log.d("OrderNumScreen", "Showing InsufficientBalance error")
                ErrorBox("결제 실패", "보유 잔수가 부족합니다") {
                    userCodeViewModel.errorState.value = UserCodeViewModel.UiError.None
                    showError = false
                    currentError = UserCodeViewModel.UiError.None
                    navController.navigate("cart")
                }
                return
            }
            is UserCodeViewModel.UiError.OutOfStock -> {
                Log.d("OrderNumScreen", "Showing OutOfStock error")
                ErrorBox("결제 실패", "재고가 부족합니다") {
                    userCodeViewModel.errorState.value = UserCodeViewModel.UiError.None
                    showError = false
                    currentError = UserCodeViewModel.UiError.None
                }
                return
            }
            is UserCodeViewModel.UiError.Generic -> {
                Log.d("OrderNumScreen", "Showing Generic error: ${error.message}")
                ErrorBox("결제 실패", error.message) {
                    userCodeViewModel.errorState.value = UserCodeViewModel.UiError.None
                    showError = false
                    currentError = UserCodeViewModel.UiError.None
                }
                return
            }
            is UserCodeViewModel.UiError.Exception -> {
                Log.d("OrderNumScreen", "Showing Exception error: ${error.throwable.message}")
                ErrorBox("예외 발생", "다시 한 번 시도해 주세요") {
                    userCodeViewModel.errorState.value = UserCodeViewModel.UiError.None
                    showError = false
                    currentError = UserCodeViewModel.UiError.None
                }
                return
            }
        }
    }

    val density = LocalDensity.current
    val barbtnSpace = with(density) {60f.toDp()}
    val widDp = with(density) {1231f.toDp()}
    val heightDp = with(density) {824f.toDp()}
    val roundDp = with(density) {32f.toDp()}
    val spaceDp = with(density) {61f.toDp()}


    val titleSp = with(density) {36f.toSp()}
    val spacerDp = with(density) {16f.toDp()}
    val descSp = with(density) {20f.toSp()}

    var selected by remember { mutableStateOf("phone") }

    // 탭 변경 시 userCode 초기화
    LaunchedEffect(selected) {
        userCodeViewModel.clear()
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ){
        TopBar(4, listOf(stringResource(R.string.top_1),
            stringResource(R.string.top_2),
            stringResource(R.string.top_3),
            stringResource(R.string.top_4)), navController, cartViewModel = cartViewModel)
        Spacer(modifier = Modifier.height(barbtnSpace))
        
        Column (
            modifier = Modifier
                .width(widDp)
                .height(heightDp)
                .background(color = Color.White, shape = RoundedCornerShape(roundDp))
                .border(2.dp, cardPayGray, RoundedCornerShape(roundDp)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){

            Row (
                modifier = Modifier.fillMaxWidth()
            ){
                PaymentTab(
                    text = "전화번호로 조회",
                    isSelected = selected == "phone",
                    onClick = { selected = "phone"},
                )
                PaymentTab(
                    text = "주문번호로 조회",
                    isSelected = selected == "order",
                    onClick = { selected = "order"},
                )


            }

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (selected) {
                    "phone" -> {
                        Text(
                            text = "휴대폰 번호 뒷 8자리를 입력해주세요",
                            style = TextStyle(
                                fontSize = titleSp,
                                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                                color = black
                            )
                        )

                        Spacer(modifier = Modifier.height(roundDp))

                        PhonePayCard(
                            title = "010 제외하고 입력",
                            viewModel = userCodeViewModel,
                            onSubmit = { phoneInput ->
                                // 결제 방법을 "phone"으로 설정
                                userCodeViewModel.selectedPaymentMethod.value = "phone"
                                userCodeViewModel.loginByPhone(
                                    apiKey = apiKey,
                                    phoneInput = phoneInput
                                ) { success ->
                                    if (success) {
                                        navController.navigate("userInfo")
                                    }
                                }
                            }
                        )
                    }
                    "order" -> {
                        Text(
                            text = stringResource(R.string.pay_order_desc),
                            style = TextStyle(
                                fontSize = titleSp,
                                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                                color = black
                            )
                        )

                        Spacer(modifier = Modifier.height(roundDp))

                        PhonePayCard(
                            title = "주문번호를 입력해주세요",
                            viewModel = userCodeViewModel,
                            onSubmit = { orderNumber ->
                                // 결제 방법을 "order"로 설정
                                userCodeViewModel.selectedPaymentMethod.value = "order"
                                userCodeViewModel.fetchUser(apiKey) { success ->
                                    if (success) {
                                        navController.navigate("userInfo")
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f)) // 아래로 밀기

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(R.string.pay_order_add),
                style = TextStyle(
                    fontSize = descSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = mainRed
                )
            )
        }


    }
}

@Preview(showBackground = true, device = "spec:width=1920px,height=1080px,dpi=82")
@Composable
fun OrderNumScreenPreview() {
    OrderNumScreen(navController = rememberNavController(), apiKey = "test")
}

