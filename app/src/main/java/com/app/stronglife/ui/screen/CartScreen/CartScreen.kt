package com.app.stronglife.ui.screen.CartScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.background
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.viewmodel.MenuScreenViewModel
import java.nio.file.WatchEvent
import kotlin.contracts.contract


@Composable
fun CartScreen(viewModel: CartViewModel, navController: NavController) {
    val cartItems by viewModel.cartItems


    val density = LocalDensity.current
    val roundDp = with(density) {28f.toDp()}
    val titleSp = with(density) { 44f.toSp() }
    val spaceDp = with(density) { 24f.toDp() }
    val widthDp = with(density) { 1760f.toDp() }
    val bigSpaceDp = with(density) { 71f.toDp() }


    Column (
        modifier = Modifier.background(background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopBar(step = 3, listOf("섭취시점 선택", "메뉴선택", "주문 확인"), navController)
        Spacer(modifier = Modifier.height(bigSpaceDp))

        Column(
            modifier = Modifier
                .width(widthDp),

        ) {
            ManyCartBox(cartItems, viewModel)
        }


    }
}