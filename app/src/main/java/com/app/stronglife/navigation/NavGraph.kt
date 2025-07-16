package com.app.stronglife.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.app.stronglife.ui.screen.menuScreen.AddOrCartScreen
import com.app.stronglife.ui.screen.menuScreen.MenuScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.stronglife.ui.screen.firstScreen.FirstScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "first"
    ) {
        composable("first") {
            FirstScreen(navController = navController)
        }
        composable("menu") {
            MenuScreen(navController = navController)
        }
        composable("addOrCart") {
            AddOrCartScreen(navController = navController)
        }
    }
}

