package com.app.stronglife.ui.screen.CartScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.stronglife.R
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.background
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.descGray
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.viewmodel.UserCodeViewModel
import com.app.stronglife.viewmodel.UserCodeViewModelFactory


@Composable
fun CartScreen(viewModel: CartViewModel, navController: NavController) {
    val cartItems by viewModel.cartItems
    
    val userCodeViewModel = UserCodeViewModel.getInstance(RetrofitClient.api)

    val density = LocalDensity.current
    val roundDp = with(density) { 28f.toDp() }
    val titleSp = with(density) { 44f.toSp() }
    val spaceDp = with(density) { 24f.toDp() }
    val widthDp = with(density) { 1760f.toDp() }
    val heighttoDp = with(density) { 605f.toDp() }
    val bigSpaceDp = with(density) { 71f.toDp() }
    val spacebtnDp = with(density) { 65f.toDp() }

    val verPad = with(density) { 32f.toDp() }
    val textSp = with(density) { 70f.toSp() }
    val smallTextSp = with(density) { 36f.toSp() }
    val pad2btn = with(density) { 153f.toDp() }

    val round2Dp = with(density) { 48f.toDp() }
    val btnWid = with(density) { 374f.toDp() }
    val btnHei = with(density) { 95f.toDp() }

    Column(
        modifier = Modifier
            .background(background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(
            step = 3,
            pageNames = listOf("섭취시점 선택", "메뉴선택", "주문 확인"),
            navController = navController,
            cartViewModel = viewModel
        )
        Spacer(modifier = Modifier.height(bigSpaceDp))


        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "장바구니에 담긴 상품이 없어요",
                        fontSize = textSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                        color = black,
                    )
                    Spacer(modifier = Modifier.height(verPad))
                    Text(
                        text = "원하는 보충제를 장바구니에 담고 주문해 보세요",
                        fontSize = smallTextSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = descGray
                    )
                    Spacer(modifier = Modifier.height(pad2btn))
                    Box(
                        modifier = Modifier
                            .size(btnWid, btnHei)
                            .background(mainRed, RoundedCornerShape(round2Dp))
                            .clickable { navController.navigate("menu") },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "보충제 보러가기",
                            fontSize = smallTextSp,
                            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                            color = Color.White
                        )
                    }
                }
            }
        } else {
            // ✅ cartItems 있을 때
            Column(
                modifier = Modifier
                    .width(widthDp)
                    .height(heighttoDp),
            ) {
                Text(
                    text = "주문 내역",
                    style = TextStyle(
                        fontSize = titleSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        fontWeight = FontWeight.Bold,
                        color = black
                    ),
                )
                Spacer(modifier = Modifier.height(spaceDp))
                ManyCartBox(cartItems, viewModel)
            }
            Spacer(modifier = Modifier.height(spacebtnDp))
            Column(
                modifier = Modifier.width(widthDp)
            ) {
                UnderBtn(navController, userCodeViewModel)
            }
        }
    }
}
