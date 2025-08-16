package com.app.stronglife.ui.component

import CartViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.R
import com.app.stronglife.ui.screen.firstScreen.NumberCircleWithText
import com.app.stronglife.ui.theme.mainRed
import androidx.compose.ui.res.painterResource

@Composable
fun TopBar(step:Int, pageNames:List<String>, navController: NavController, cartViewModel: CartViewModel) {
    val density = LocalDensity.current
    val heightInDp = with(density) { 112f.toDp() }
    val paddingInDp = with(density) {60f.toDp()}
    val IconPadInDp = with(density) {24f.toDp()}
    val startPaddingInDp = with(density) {20f.toDp()}
    val btnInDp = with(density) {48f.toDp()}

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(heightInDp)
            .background(mainRed)
            .padding(horizontal = paddingInDp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = startPaddingInDp)) {
            pageNames.forEachIndexed { index, title ->
                if (index > 0) Spacer(modifier = Modifier.width(50.dp))
                NumberCircleWithText(
                    number = (index + 1).toString(),
                    title = title,
                    isActive = (index == pageNames.lastIndex),
                    textSizeSp = 36f
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically){

            Box {
                Image(
                    painter = painterResource(id = R.drawable.cart),
                    modifier = Modifier.size(btnInDp)
                        .clickable{navController.navigate("cart")},
                    contentDescription = "장바구니 이동 버튼"
                )
                
                // Cart 아이템 개수가 1개 이상일 때만 CartAlertBox 표시
                val cartItemCount = cartViewModel.cartItems.value.sumOf { it.quantity }
                if (cartItemCount > 0) {
                    CartAlertBox(
                        number = cartItemCount,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    )
                }
            }

            Spacer(modifier = Modifier.width(IconPadInDp))

            Image(
                painter = painterResource(id = R.drawable.home),
                modifier = Modifier.size(btnInDp)
                    .clickable{navController.navigate("hello")},
                contentDescription = "처음으로 이동 버튼"
            )
        }
    }
}


