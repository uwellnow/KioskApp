package com.app.stronglife.ui.screen.CartScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.R
import com.app.stronglife.ui.theme.lightGray
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.superLightGray
import com.app.stronglife.viewmodel.UserCodeViewModel

@Composable
fun UnderBtn(navController: NavController, userCodeViewModel: UserCodeViewModel) {
    val density = LocalDensity.current
    val couponWidDp = with(density) {770f.toDp()}
    val payWidDp = with(density) {840f.toDp()}
    val heightDp = with(density) {110f.toDp()}
    val couponSp = with(density) {36f.toSp()}
    val paySp = with(density) {40f.toSp()}
    val roundDp = with(density) {20f.toDp()}
    val spaceDp = with(density) {65f.toDp()}

    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(roundDp)
    ){
        Box(
            modifier = Modifier
                .width(heightDp)
                .height(heightDp)
                .background(superLightGray,
                    shape = RoundedCornerShape(roundDp)
                )
                .clickable{navController.navigate("menu")},
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = "뒤로가기",
                modifier = Modifier.size(spaceDp)
            )
        }

        Box(
            modifier = Modifier
                .width(couponWidDp)
                .height(heightDp)
                .background(
                    Color.Transparent,
                    shape = RoundedCornerShape(roundDp)
                )
                .border(2.dp, color = mainRed, shape = RoundedCornerShape(roundDp))
                .clickable { 
                    userCodeViewModel.setPaymentMethodId(1)
                    navController.navigate("pay_coupon")
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.use_coupon),
                style = TextStyle(
                    fontSize = couponSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    fontWeight = FontWeight.SemiBold,
                    color = mainRed
                )
            )
        }

        Box(
            modifier = Modifier
                .width(payWidDp)
                .height(heightDp)
                .background(
                    mainRed,
                    shape = RoundedCornerShape(roundDp)
                )
                .clickable { 
                    userCodeViewModel.setPaymentMethodId(2)
                    navController.navigate("pay_number")
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.pay),
                style = TextStyle(
                    fontSize = paySp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            )
        }
    }
}

