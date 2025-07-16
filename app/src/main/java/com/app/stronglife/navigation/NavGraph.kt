package com.app.stronglife.navigation

import CartViewModel
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.app.stronglife.ui.screen.menuScreen.AddOrCartScreen
import com.app.stronglife.ui.screen.menuScreen.MenuScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.stronglife.ui.screen.CartScreen.CartScreen
import com.app.stronglife.ui.screen.firstScreen.FirstScreen

@Composable
fun NavGraph(navController: NavHostController, cartViewModel: CartViewModel) {

    NavHost(
        navController = navController,
        startDestination = "first"
    ) {
        composable("first") {
            FirstScreen(navController = navController)
        }
        composable("menu") {
            MenuScreen(navController = navController, cartViewModel = cartViewModel)
        }
        composable("addOrCart") {
            AddOrCartScreen(navController = navController)
        }
        composable("cart") {
            CartScreen(viewModel = cartViewModel,navController = navController)
        }
    }
}

