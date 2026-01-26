package com.app.stronglife.navigation

import CartViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.app.stronglife.ui.screen.menuScreen.AddOrCartScreen
import com.app.stronglife.ui.screen.menuScreen.MenuScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.stronglife.ui.screen.CartScreen.CartScreen
import com.app.stronglife.ui.screen.EndScreen.EndScreen
import com.app.stronglife.ui.screen.HelloScreen.HelloScreen
import com.app.stronglife.ui.screen.PayScreen.CouponScreen
import com.app.stronglife.ui.screen.PayScreen.OrderNumScreen
import com.app.stronglife.ui.screen.UserInfoScreen.UserInfoScreen
import com.app.stronglife.ui.screen.PaySelectScreen.PaySelectScreen
import com.app.stronglife.ui.screen.PayingScreen.PayingScreen
import com.app.stronglife.ui.screen.firstScreen.FirstScreen
import com.app.stronglife.viewmodel.ProductViewModel
import com.app.stronglife.ui.screen.HelloScreen.RegisterStoreScreen
import com.app.stronglife.ui.screen.firstScreen.FirstPickScreen
import com.app.stronglife.ui.screen.firstScreen.PickPurposeScreen
import com.app.stronglife.ui.screen.menuScreen.PickMenuScreen
import com.app.stronglife.util.LanguageManager
import com.app.stronglife.viewmodel.UserCodeViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


@Composable
fun NavGraph(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    productViewModel: ProductViewModel,
    userViewModel: UserCodeViewModel,
    apiKey: String,
    onApiKeyChanged: (String) -> Unit,
    languageManager: LanguageManager
) {

    // startDestination은 초기값만 사용 (재구성 시 변경되지 않도록)
    val start = remember { 
        if (apiKey.isNotEmpty()) "hello" else "register" 
    }

    NavHost(
        navController = navController,
        startDestination = start
    ) {
        composable("register") {
            RegisterStoreScreen(
                navController = navController,
                userCodeViewModel = userViewModel,
                onApiKeySet = { newApiKey ->
                    onApiKeyChanged(newApiKey)
                    // 라우팅은 RegisterStoreScreen에서 is_picked 값에 따라 처리됨
                }
            )
        }
        composable("hello") {
            HelloScreen(navController = navController, cartViewModel, userViewModel, apiKey, languageManager)
        }
        composable("first") {
            FirstScreen(navController = navController, cartViewModel = cartViewModel)
        }
        composable("first_pick") {
            FirstPickScreen(navController = navController, cartViewModel = cartViewModel)
        }
        composable("pick_purpose") {
            PickPurposeScreen(
                cartViewModel = cartViewModel, 
                navController = navController,
                productViewModel = productViewModel,
                apiKey = apiKey
            )
        }
        composable(
            route = "pick_menu/{purpose}",
            arguments = listOf(navArgument("purpose") { type = NavType.StringType })
        ) { backStackEntry ->
            val purpose = URLDecoder.decode(
                backStackEntry.arguments?.getString("purpose") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            PickMenuScreen(
                viewModel = productViewModel,
                navController = navController,
                cartViewModel = cartViewModel,
                apiKey = apiKey,
                purpose = purpose,
                languageManager = languageManager
            )
        }
        composable("menu") {
            MenuScreen(
                viewModel = productViewModel, 
                navController = navController, 
                cartViewModel = cartViewModel,
                apiKey = apiKey,
                languageManager = languageManager
            )
        }
        composable("addOrCart") {
            AddOrCartScreen(navController = navController, cartViewModel = cartViewModel)
        }
        composable("cart") {
            CartScreen(viewModel = cartViewModel,navController = navController, languageManager = languageManager)
        }
        composable("paySelect") {
            PaySelectScreen(navController = navController, cartViewModel = cartViewModel)
        }

        composable("pay_coupon") {
            CouponScreen(navController = navController, apiKey = apiKey, cartViewModel = cartViewModel)
        }
        composable("pay_number") {
            OrderNumScreen(navController = navController, apiKey = apiKey, cartViewModel = cartViewModel)
        }
        composable("userInfo") {
            UserInfoScreen(navController = navController, apiKey = apiKey, cartViewModel = cartViewModel)
        }
        composable("paying") {
            PayingScreen(viewModel = cartViewModel, userViewModel = userViewModel, navController)
        }
        composable("End") {
            EndScreen(navController = navController, productViewModel, cartViewModel, apiKey = apiKey)
        }

    }
}

