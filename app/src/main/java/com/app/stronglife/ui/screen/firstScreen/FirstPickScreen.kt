package com.app.stronglife.ui.screen.firstScreen

import android.view.RoundedCorner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.R
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.lightRed
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.shadowGray
import CartViewModel

@Composable
fun FirstPickScreen(navController: NavController, cartViewModel: CartViewModel) {
    // 장바구니 비우기 (hello 화면과 동일하게)
    LaunchedEffect(Unit) {
        cartViewModel.clearCart()
    }
    val density = LocalDensity.current
    val horPadding = with(density) {112f.toDp()}
    val verPadding = with(density) {170f.toDp()}
    val titleSp = with(density) {100f.toSp()}
    val descSp = with(density) {32f.toSp()}
    val space1dp = with(density) {28f.toDp()}
    val space2dp = with(density) {67f.toDp()}

    val boxWidth = with(density) {824f.toDp()}
    val boxHeight = with(density) {440f.toDp()}
    val image1Width = with(density) {200f.toDp()}
    val image1Height = with(density) {217f.toDp()}
    val image2Width = with(density) {149f.toDp()}
    val image2Height = with(density) {266f.toDp()}
    val boxTitleSp = with(density) {56f.toSp()}
    val boxTitle2Sp= with(density) {60f.toSp()}
    val boxDescSp = with(density) {36f.toSp()}
    val boxDesc2Sp = with(density) {40f.toSp()}

    val boxSpace1Dp = with(density) {40f.toDp()}
    val boxSpace2Dp = with(density) {24f.toDp()}
    val boxSpaceDp = with(density) {48f.toDp()}
    val blurRadiusPx = with(density) { 24.dp.toPx() }
    val boxRoundDp = with(density) {32f.toDp()}

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(horPadding, verPadding)
    ) {
        Text(
            text = "기존에 먹던 파우더 보충제를\n30초만에 음료로 바로!",
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = titleSp,
            color = black
        )
        Spacer(modifier = Modifier.height(space1dp))
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF939393)
                    )
                ) {
                    append("이제 ")
                }

                withStyle(
                    style = SpanStyle(
                        color = mainRed
                    )
                ) {
                    append("물, 쉐이커, 스트레스 없이")
                }

                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF939393)
                    )
                ) {
                    append(", 운동 루틴 그대로.")
                }
            },
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = descSp
        )
        Spacer(modifier = Modifier.height(space2dp))
        Row (
            horizontalArrangement = Arrangement.spacedBy(boxSpaceDp)
        ){
            Box(
                modifier = Modifier.size(boxWidth, boxHeight)
                    .drawBehind {
                        drawIntoCanvas { canvas ->
                            val paint = Paint().asFrameworkPaint().apply {
                                color = shadowGray.toArgb()
                                maskFilter = android.graphics.BlurMaskFilter(
                                    blurRadiusPx,
                                    android.graphics.BlurMaskFilter.Blur.NORMAL
                                )
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
                    .background(color = Color.White, shape = RoundedCornerShape(boxRoundDp))
                    .clickable{navController.navigate("pick_purpose")},
            ){
                Row(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = 44.dp, vertical = 112.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.brand_pick),
                        contentDescription = "브랜드 픽",
                        modifier = Modifier.size(image1Width, image1Height)
                    )
                    Spacer(modifier = Modifier.width(44.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(boxSpace1Dp)
                    ) {
                        Text(
                            text = "사과코치 Pick",
                            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                            fontSize = boxTitleSp,
                            color = black
                        )
                        Text(
                            text = "목표·운동에 맞춰, 검증된\n조합으로 빠르게 끝내기",
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = boxDescSp,
                            color = Color(0xFF949494)
                        )
                    }
                }

            }


            Box(
                modifier = Modifier.size(boxWidth, boxHeight)
                    .drawBehind {
                        drawIntoCanvas { canvas ->
                            val paint = Paint().asFrameworkPaint().apply {
                                color = shadowGray.toArgb()
                                maskFilter = android.graphics.BlurMaskFilter(
                                    blurRadiusPx,
                                    android.graphics.BlurMaskFilter.Blur.NORMAL
                                )
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
                    .background(color = Color.White, shape = RoundedCornerShape(boxRoundDp))
                    .clickable{navController.navigate("menu")},
            ){
                Row(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = 70.dp, vertical = 87.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.choose_pick),
                        contentDescription = "스스로 픽",
                        modifier = Modifier.size(image2Width, image2Height)
                    )
                    Spacer(modifier = Modifier.width(60.dp))
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "내가 직접 선택하기",
                            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                            fontSize = boxTitleSp,
                            color = black
                        )
                        Spacer(modifier = Modifier.height(boxSpace2Dp))
                        Text(
                            text = "원하는 제품을 직접 선택해요",
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = boxDescSp,
                            color = Color(0xFF949494)
                        )
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=1920px,height=1080px,dpi=82")
@Composable
fun FirstPickScreenPreview(){
    FirstPickScreen(navController = rememberNavController(), cartViewModel = CartViewModel())
}