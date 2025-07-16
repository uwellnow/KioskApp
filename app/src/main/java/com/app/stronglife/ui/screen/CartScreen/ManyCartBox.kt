package com.app.stronglife.ui.screen.CartScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.app.stronglife.data.model.CartItem

@Composable
fun ManyCartBox(cartItems: List<CartItem>, viewModel: CartViewModel) {
    val density = LocalDensity.current
    val roundDp = with(density) { 28f.toDp() }
    val horPad = with(density) { 60f.toDp() }
    val verPad = with(density) { 32f.toDp() }

    Column(
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(roundDp))
            .padding(horizontal = horPad, vertical = verPad)
    ) {
        cartItems.forEachIndexed { index, item ->
            OneOfCartBox(item, viewModel)

            if (index != cartItems.lastIndex) {
                Spacer(modifier = Modifier.height(16.dp)) // 위아래 여백
                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
