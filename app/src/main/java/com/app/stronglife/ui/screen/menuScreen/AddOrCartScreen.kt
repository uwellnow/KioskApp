package com.app.stronglife.ui.screen.menuScreen


import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.app.stronglife.R
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.screen.firstScreen.customShadow
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.lightGray
import com.app.stronglife.ui.theme.lightRed
import com.app.stronglife.ui.theme.midGray
import com.app.stronglife.ui.theme.shadowGray

@Composable
fun AddOrCartScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel()
) {

    val density = LocalDensity.current
    val textToSp = with(density) {40f.toSp()}
    val btnToDp = with(density) {600f.toDp()}
    val imageToDp = with(density) {358.toDp()}
    val spacetoDp = with(density) {56f.toDp()}
    val imagetoTextDp = with(density) {32f.toDp()}
    val borderRadiusPx = with(density) { imagetoTextDp.toPx() }
    val blurRadiusPx = with(density) { 24.dp.toPx() }

    Column {
        TopBar(step = 2, listOf("섭취지점 선택", "메뉴선택"), navController = navController, cartViewModel = cartViewModel)
        Row (
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column (
                modifier = Modifier
                    .width(btnToDp)
                    .height(btnToDp)
                    .drawBehind {
                        drawIntoCanvas { canvas ->
                            val paint = Paint().asFrameworkPaint().apply {
                                color = shadowGray.toArgb()
                                maskFilter = android.graphics.BlurMaskFilter(blurRadiusPx, android.graphics.BlurMaskFilter.Blur.NORMAL)
                            }
                            canvas.nativeCanvas.drawRoundRect(
                                0f,
                                0f,
                                size.width,
                                size.height,
                                blurRadiusPx,
                                blurRadiusPx,
                                paint
                            )
                        }
                    }
                    .background(color = Color.White, shape = RoundedCornerShape(imagetoTextDp))
                    .clickable{navController.navigate("menu")}
                    ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                AsyncImage(
                    model = R.drawable.addorder,
                    contentDescription = "추가 주문하기",
                    modifier = Modifier
                        .width(imageToDp)
                        .height(imageToDp)
                )
                Spacer(modifier = Modifier.height(imagetoTextDp))
                Text(
                    text = "추가 주문하기",
                    style = TextStyle(
                        fontSize = textToSp,
                        fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                        fontWeight = FontWeight.Bold,
                        color = black
                    )
                )
            }

            Spacer(modifier = Modifier.width(spacetoDp))

            Column(
                modifier = Modifier
                    .width(btnToDp)
                    .height(btnToDp)
                    .drawBehind {
                        drawIntoCanvas { canvas ->
                            val paint = Paint().asFrameworkPaint().apply {
                                color = shadowGray.toArgb()
                                maskFilter = android.graphics.BlurMaskFilter(blurRadiusPx, android.graphics.BlurMaskFilter.Blur.NORMAL)
                            }
                            canvas.nativeCanvas.drawRoundRect(
                                0f,
                                0f,
                                size.width,
                                size.height,
                                blurRadiusPx,
                                blurRadiusPx,
                                paint
                            )
                        }
                    }
                    .background(color = Color.White, shape = RoundedCornerShape(imagetoTextDp))
                    .clickable{navController.navigate("cart")}
                    ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                AsyncImage(
                    model = R.drawable.gocart,
                    contentDescription = "장바구니로 이동",
                    modifier = Modifier
                        .width(imageToDp)
                        .height(imageToDp)

                )
                Spacer(modifier = Modifier.height(imagetoTextDp))
                Text(
                    text = "장바구니로 이동",
                    style = TextStyle(
                        fontSize = textToSp,
                        fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                        fontWeight = FontWeight.Bold,
                        color = black
                    )
                )
            }
        }
    }
}
