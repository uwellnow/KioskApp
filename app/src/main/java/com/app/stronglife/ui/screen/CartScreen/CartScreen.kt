package com.app.stronglife.ui.screen.CartScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.stronglife.R
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.background
import com.app.stronglife.ui.theme.black


@Composable
fun CartScreen(viewModel: CartViewModel, navController: NavController) {
    val cartItems by viewModel.cartItems


    val density = LocalDensity.current
    val roundDp = with(density) {28f.toDp()}
    val titleSp = with(density) { 44f.toSp() }
    val spaceDp = with(density) { 24f.toDp() }
    val widthDp = with(density) { 1760f.toDp() }
    val heighttoDp = with(density) {605f.toDp()}
    val bigSpaceDp = with(density) { 71f.toDp() }
    val spacebtnDp = with(density) {65f.toDp()}


    Column (
        modifier = Modifier.background(background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopBar(step = 3, listOf("섭취시점 선택", "메뉴선택", "주문 확인"), navController)
        Spacer(modifier = Modifier.height(bigSpaceDp))

        Column(
            modifier = Modifier
                .width(widthDp)
                .height(heighttoDp),

        ) {
            Text(
                text = "주문 내역",
                style = TextStyle(
                    fontSize = titleSp,
                    fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                    fontWeight = FontWeight.Bold,
                    color = black
                ),

            )
            Spacer(modifier = Modifier.height(spaceDp))
            ManyCartBox(cartItems, viewModel)
        }
        Spacer(modifier = Modifier.height(spacebtnDp))
        Column (
            modifier = Modifier.width(widthDp)
        ){
            UnderBtn(navController)
        }



    }
}