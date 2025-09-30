package com.app.stronglife.ui.screen.RecipeScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.stronglife.R
import com.app.stronglife.data.model.CartItem
import com.app.stronglife.ui.screen.PayScreen.KeyPad
import com.app.stronglife.ui.theme.background
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.lightGray
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.midGray
import com.app.stronglife.viewmodel.ProductViewModel
import com.app.stronglife.viewmodel.UserCodeViewModel
import android.util.Log

@Composable
fun RecipeInputCard(
    navController: NavController,
    viewModel: UserCodeViewModel,
    productViewModel: ProductViewModel,
    apiKey: String,
    cartViewModel: CartViewModel,
    onRecipeSuccess: () -> Unit
) {
    val density = LocalDensity.current
    val widDp = with(density) { 584f.toDp() }
    val textSp = with(density) { 36f.toSp() }
    val roundDp = with(density) { 12f.toDp() }
    val boxWidDp = with(density) { 97f.toDp() }
    val boxHeiDp = with(density) { 44f.toDp() }
    val boxTextSp = with(density) { 24f.toSp() }
    val spacerDp = with(density) { 60f.toDp() }
    val spacer2Dp = with(density) { 17f.toDp() }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(widDp).padding(horizontal = 40.dp)
        ) {
            BasicTextField(
                value = viewModel.userCode.value,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() } && newValue.length <= 12) {
                        viewModel.userCode.value = newValue
                    }
                },
                textStyle = TextStyle(
                    fontSize = textSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontWeight = FontWeight.Medium,
                    color = black,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.width(widDp - boxWidDp - spacerDp - 80.dp)
            )

            Spacer(modifier = Modifier.width(spacerDp))

            val isFilled = viewModel.userCode.value.isNotEmpty()

            Box(
                modifier = Modifier
                    .background(
                        if (viewModel.userCode.value.isNotEmpty()) mainRed else background,
                        shape = RoundedCornerShape(roundDp)
                    )
                    .size(boxWidDp, boxHeiDp)
                    .clickable(enabled = isFilled) {
                        if (viewModel.userCode.value.isNotEmpty()) {
                            Log.d("RecipeInputCard", "조회 버튼 클릭 - recipe_code: ${viewModel.userCode.value}")
                            
                            // 레시피 조회 요청
                            viewModel.getRecipe(
                                apiKey = apiKey,
                                recipeCode = viewModel.userCode.value
                            ) { recipeResponse ->
                                Log.d("RecipeInputCard", "레시피 응답: $recipeResponse")
                                
                                if (recipeResponse != null) {
                                    Log.d("RecipeInputCard", "상품 ID: ${recipeResponse.productId}, 사용자: ${recipeResponse.userName}")
                                    
                                    // product_id로 상품 찾기
                                    val product = productViewModel.products.find { 
                                        it.id == recipeResponse.productId 
                                    }
                                    
                                    if (product != null) {
                                        Log.d("RecipeInputCard", "상품 찾음: ${product.name}")
                                        
                                        // 장바구니 비우고 레시피 상품 추가
                                        Log.d("RecipeInputCard", "장바구니 비우기 전: ${cartViewModel.cartItems.value.size}개 상품")
                                        cartViewModel.clearCart()
                                        Log.d("RecipeInputCard", "장바구니 비운 후: ${cartViewModel.cartItems.value.size}개 상품")
                                        
                                        cartViewModel.addProduct(product)
                                        Log.d("RecipeInputCard", "상품 추가 후: ${cartViewModel.cartItems.value.size}개 상품")
                                        Log.d("RecipeInputCard", "장바구니 내용: ${cartViewModel.cartItems.value.map { "${it.product.name} x${it.quantity}" }}")
                                        
                                        Log.d("RecipeInputCard", "PayingScreen으로 이동 시작")
                                        
                                        // PayingScreen으로 이동
                                        try {
                                            navController.navigate("paying")
                                            Log.d("RecipeInputCard", "navigate 호출 완료")
                                        } catch (e: Exception) {
                                            Log.e("RecipeInputCard", "Navigate 실패: ${e.message}", e)
                                        }
                                    } else {
                                        Log.e("RecipeInputCard", "상품을 찾을 수 없음 - product_id: ${recipeResponse.productId}")
                                        Log.e("RecipeInputCard", "사용 가능한 상품들: ${productViewModel.products.map { it.id }}")
                                    }
                                } else {
                                    Log.e("RecipeInputCard", "레시피 응답이 null입니다")
                                }
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.number_btn),
                    style = TextStyle(
                        fontSize = boxTextSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontWeight = FontWeight.Medium,
                        color = if (viewModel.userCode.value.isNotEmpty()) Color.White else midGray
                    )
                )
            }
        }

        Divider(
            modifier = Modifier.width(widDp).padding(top = spacer2Dp),
            color = lightGray
        )

        Spacer(modifier = Modifier.height(spacerDp))

        KeyPad(
            onNumberClick = { digit -> viewModel.addDigit(digit) },
            onDeleteClick = { viewModel.removeLast() },
            onClearClick = { viewModel.clear() }
        )
    }
}
