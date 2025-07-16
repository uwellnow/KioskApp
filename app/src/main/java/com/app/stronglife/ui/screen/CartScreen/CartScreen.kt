package com.app.stronglife.ui.screen.CartScreen

import CartViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalDensity
import androidx.navigation.NavController
import com.app.stronglife.viewmodel.MenuScreenViewModel

@Composable
fun ToDp (size:Int) {
    val density = LocalDensity.current
    return with(density) {size.toDp()}
}

@Composable
fun ToSp (size:Int) {
    val density = LocalDensity.current
    return with(density) {size.toSp()}
}

@Composable
fun CartScreen(viewModel: CartViewModel, navController: NavController) {
    val cartItems by viewModel.cartItems

    val cartTitle = ToSp(44)
    val spaceToDp = ToDp(24)



}