package com.app.stronglife.ui.screen.PayingScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.ui.component.TopBar

@Composable
fun PayingScreen(navController: NavController) {
    Column {
        TopBar(4, listOf("섭취시점 선택", "메뉴선택", "주문 확인", "결제하기"), navController)

    }
}

@Preview
@Composable
fun PScPreview() {
    PayingScreen(navController = rememberNavController())
}