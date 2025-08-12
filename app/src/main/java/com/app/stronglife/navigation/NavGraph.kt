package com.app.stronglife.navigation

import CartViewModel
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.app.stronglife.ui.screen.menuScreen.AddOrCartScreen
import com.app.stronglife.ui.screen.menuScreen.MenuScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.stronglife.ui.screen.CartScreen.CartScreen
import com.app.stronglife.ui.screen.EndScreen.EndScreen
import com.app.stronglife.ui.screen.HelloScreen.HelloScreen
import com.app.stronglife.ui.screen.PayScreen.PayOverlayCard
import com.app.stronglife.ui.screen.PayScreen.PayScreen
import com.app.stronglife.ui.screen.PaySelectScreen.PaySelectScreen
import com.app.stronglife.ui.screen.PayingScreen.PayingScreen
import com.app.stronglife.ui.screen.firstScreen.FirstScreen
import com.app.stronglife.viewmodel.ProductViewModel

@Composable
fun NavGraph(navController: NavHostController, cartViewModel: CartViewModel, productViewModel: ProductViewModel) {

    NavHost(
        navController = navController,
        startDestination = "hello"
    ) {

        composable("hello") {
            HelloScreen(navController = navController)
        }
        composable("first") {
            FirstScreen(navController = navController)
        }
        composable("menu") {
            MenuScreen(viewModel = productViewModel, navController = navController, cartViewModel = cartViewModel)
        }
        composable("addOrCart") {
            AddOrCartScreen(navController = navController)
        }
        composable("cart") {
            CartScreen(viewModel = cartViewModel,navController = navController)
        }
        composable("paySelect") {
            PaySelectScreen(navController = navController)
        }
        composable("pay") {
            PayScreen(navController = navController)
        }
        composable("paying") {
            PayingScreen(navController = navController)
        }
        composable("End") {
            EndScreen(navController = navController)
        }

    }
}

