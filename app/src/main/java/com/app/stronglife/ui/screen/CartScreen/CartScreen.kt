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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.app.stronglife.R
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.background
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.descGray
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.viewmodel.UserCodeViewModel


@Composable
fun CartScreen(viewModel: CartViewModel, navController: NavController, languageManager: com.app.stronglife.util.LanguageManager) {
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
        TopBar(3, listOf(stringResource(R.string.top_1),
            stringResource(R.string.top_2),
            stringResource(R.string.top_3)), navController, cartViewModel = viewModel)
        Spacer(modifier = Modifier.height(bigSpaceDp))


        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.no_cart_title),
                        fontSize = textSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                        color = black,
                    )
                    Spacer(modifier = Modifier.height(verPad))
                    Text(
                        text = stringResource(R.string.no_cart_desc),
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
                            text = stringResource(R.string.go_menu),
                            fontSize = smallTextSp,
                            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                            color = Color.White
                        )
                    }
                }
            }
        } else {
            /**
             * cartItems 있을 때
             */
            Column(
                modifier = Modifier
                    .width(widthDp)
                    .height(heighttoDp),
            ) {
                Text(
                    text = stringResource(R.string.orders),
                    style = TextStyle(
                        fontSize = titleSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        fontWeight = FontWeight.Bold,
                        color = black
                    ),
                )
                Spacer(modifier = Modifier.height(spaceDp))
                ManyCartBox(cartItems, viewModel, languageManager)
            }
            Spacer(modifier = Modifier.height(spacebtnDp))
            Column(
                modifier = Modifier.width(widthDp)
            ) {
                UnderBtn(navController, userCodeViewModel, cartItems)
            }
        }
    }
}
