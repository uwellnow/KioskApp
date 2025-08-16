package com.app.stronglife.ui.screen.CartScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.app.stronglife.R
import com.app.stronglife.data.model.CartItem
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.shadowGray

@Composable
fun ManyCartBox(cartItems: List<CartItem>, viewModel: CartViewModel) {
    val density = LocalDensity.current
    val roundDp = with(density) { 28f.toDp() }
    val horPad = with(density) { 60f.toDp() }
    val verPad = with(density) { 32f.toDp() }
    val textSp = with(density) {80f.toSp()}

    val blurRadiusPx = with(density) { 24.dp.toPx() }



    if (cartItems.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "장바구니가 비었습니다",
                fontSize = textSp,
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                color = black,
                modifier = Modifier.padding(vertical = 40.dp)
            )
        }

    } else {
        Column(
            modifier = Modifier
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
                .background(Color.White, shape = RoundedCornerShape(roundDp))
                .padding(horizontal = horPad, vertical = verPad)
                .verticalScroll(rememberScrollState())

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

    }

