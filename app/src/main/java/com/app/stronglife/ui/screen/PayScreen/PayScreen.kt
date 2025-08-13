package com.app.stronglife.ui.screen.PayScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.cardPayGray
import kotlinx.coroutines.delay

@Composable
fun PayScreen(navController: NavController) {
    val density = LocalDensity.current

    val barbtnSpace = with(density) {81f.toDp()}

    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopBar(4, listOf("섭취시점 선택", "메뉴선택", "주문 확인", "결제하기"), navController)
        Spacer(modifier = Modifier.height(barbtnSpace))
        PayOverlayCard(navController)

    }
}
