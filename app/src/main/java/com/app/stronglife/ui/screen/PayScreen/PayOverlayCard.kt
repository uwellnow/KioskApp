package com.app.stronglife.ui.screen.PayScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.mock.sampleMembers
import com.app.stronglife.ui.theme.cardPayGray
import com.app.stronglife.viewmodel.UserCodeViewModel
import com.app.stronglife.viewmodel.UserCodeViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun PayOverlayCard(navController: NavController, apiKey: String) {

    val userCodeViewModel: UserCodeViewModel = viewModel(
        factory = UserCodeViewModelFactory(RetrofitClient.api)
    )
    var isUserInfoVisible by remember { mutableStateOf(false) }
    val loginResponse = userCodeViewModel.loginResponse.value


    val density = LocalDensity.current
    val widDp = with(density) {1231f.toDp()}
    val heightDp = with(density) {824f.toDp()}
    val roundDp = with(density) {32f.toDp()}

    val spaceDp = with(density) {61f.toDp()}

    var selected by remember { mutableStateOf("QR") }

    LaunchedEffect(loginResponse) {
        if (loginResponse != null) {
            navController.navigate("paying")
        }
    }

    //Todo : scanndId 2025 하드코딩 -> 스캔된 id로
    var scannedId by remember { mutableStateOf<Int?>(2025) }
    var showInfo by remember { mutableStateOf(false) }




    //Todo : delay (X) -> qr 스캔 완료 (o)
//    LaunchedEffect(Unit) {
//        delay(3000)
//        showInfo = true
//
//        delay(2000)
//        navController.navigate("paying")
//    }

    Column (
        modifier = Modifier
            .width(widDp)
            .height(heightDp)
            .background(color = Color.White, shape = RoundedCornerShape(roundDp))
            .border(2.dp, cardPayGray, RoundedCornerShape(roundDp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row (
            modifier = Modifier.fillMaxWidth()
        ){
            PaymentTab(
                text = "QR코드 스캔",
                isSelected = selected == "QR",
                onClick = { selected = "QR"},
            )
            PaymentTab(
                text = "주문번호로 조회",
                isSelected = selected == "phone",
                onClick = { selected = "phone"},
            )


        }
        Spacer(modifier = Modifier.height(spaceDp))

        /*Todo: 바코드 스캔하면 해당 회원의 id 가져오기 -> 밑의 파라미터에 넣으면 됨
        * Todo: 3초 타이머 부분(임시 코드) <- 통신 연결 코드 넣기 */
        when (selected) {
            "QR" -> {
                scannedId?.let { scanId ->
                    QRPayCard(inputId = scanId, showInfo = showInfo)
                }
            }
            "phone" -> {
                if (isUserInfoVisible) {
                    UserBox(
                        showInfo = userCodeViewModel.loginResponse.value != null,
                        loginResponse = userCodeViewModel.loginResponse.value
                    )
                } else {
                    PhonePayCard(
                        viewModel = userCodeViewModel,
                        apiKey = apiKey,
                    onUserFound = { isUserInfoVisible = true},
                        navController = navController
                    )
                }
                }

                }
            }
        }




