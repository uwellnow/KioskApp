package com.app.stronglife.ui.screen.PayScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.R
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.errorGray
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.viewmodel.UserCodeViewModel

@Composable
fun CouponErrorBox(navController: NavController) {
    val userCodeViewModel = UserCodeViewModel.getInstance(RetrofitClient.api)
    
    val density = LocalDensity.current
    val widDp = with(density) { 178f.toDp() }
    val roundDp = with(density) { 32f.toDp() }
    val ErrorSp = with(density) { 70f.toSp() }
    val DesSp = with(density) { 32f.toSp() }
    val spaceDp = with(density) { 56f.toDp() }
    val btnSp = with(density) { 28f.toSp() }
    val round2dp = with(density) { 48f.toDp() }
    val space2Dp = with(density) { 157f.toDp() }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.error),
                contentDescription = "에러발생",
                modifier = Modifier.size(widDp)
            )
            Spacer(modifier = Modifier.height(spaceDp))
            Text(
                text = stringResource(R.string.error_coupon_title),
                style = TextStyle(
                    fontSize = ErrorSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    color = black
                )
            )
            Spacer(modifier = Modifier.height(roundDp))
            Text(
                text = stringResource(R.string.error_coupon_desc),
                style = TextStyle(
                    fontSize = DesSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = errorGray
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(space2Dp))
            Box(
                modifier = Modifier
                    .background(color = mainRed, shape = RoundedCornerShape(round2dp))
                    .clickable {
                        userCodeViewModel.clearPurchaseError()
                        navController.popBackStack()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.retry),
                    style = TextStyle(
                        fontSize = btnSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = Color.White
                    ),
                    modifier = Modifier.padding(horizontal = 70.dp, vertical = 30.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=1920px,height=1080px,dpi=82")
@Composable
fun CouponErrorBoxPreview() {
    CouponErrorBox(navController = rememberNavController())
}

