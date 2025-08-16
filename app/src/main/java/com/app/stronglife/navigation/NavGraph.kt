package com.app.stronglife.navigation

import CartViewModel
import androidx.compose.animation.ExperimentalAnimationApi
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
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import androidx.compose.animation.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.stronglife.data.model.User
import com.app.stronglife.viewmodel.UserCodeViewModel


@Composable
fun NavGraph(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    productViewModel: ProductViewModel,
    userViewModel: UserCodeViewModel,
    apiKey: String) {

    @OptIn(ExperimentalAnimationApi::class)
    AnimatedNavHost(
        navController = navController,
        startDestination = "hello",
        enterTransition = {
            slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
        }
    ) {

        composable("hello") {
            HelloScreen(navController = navController, cartViewModel, userViewModel, apiKey)
        }
        composable("first") {
            FirstScreen(navController = navController, cartViewModel = cartViewModel)
        }
        composable("menu") {
            MenuScreen(viewModel = productViewModel, navController = navController, cartViewModel = cartViewModel)
        }
        composable("addOrCart") {
            AddOrCartScreen(navController = navController, cartViewModel = cartViewModel)
        }
        composable("cart") {
            CartScreen(viewModel = cartViewModel,navController = navController)
        }
        composable("paySelect") {
            PaySelectScreen(navController = navController, cartViewModel = cartViewModel)
        }
        composable("pay") {
            PayScreen(navController = navController, apiKey = apiKey, cartViewModel = cartViewModel)
        }
        composable("paying") {
            PayingScreen(viewModel = cartViewModel, userViewModel = userViewModel, navController)
        }
        composable("End") {
            EndScreen(navController = navController, apiKey = apiKey)
        }

    }
}

