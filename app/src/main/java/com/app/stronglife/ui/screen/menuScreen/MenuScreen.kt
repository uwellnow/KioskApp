package com.app.stronglife.ui.screen.menuScreen

import CartViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.background
import com.app.stronglife.viewmodel.ProductViewModel

@Composable
fun MenuScreen(
    viewModel: ProductViewModel,
    cartViewModel: CartViewModel = viewModel(),
    navController: NavController = rememberNavController()
) {


    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage
    val currentDetail = viewModel.currentDetail

    LaunchedEffect(Unit) {
        viewModel.fetchProducts()
    }

    val density = LocalDensity.current
    val spacertoDp = with(density) { 80f.toDp() }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 메인 콘텐츠
        Column(
            modifier = Modifier
                .alpha(if (currentDetail != null) 0.3f else 1f)
                .fillMaxSize()
                .background(background)
        ) {
            TopBar(
                step = 2,
                listOf("섭취시점 선택", "메뉴선택"),
                navController = navController,
                cartViewModel = cartViewModel
            )
            Spacer(modifier = Modifier.height(spacertoDp))
            when {
                isLoading -> {
                    Text("로딩 중...")
                }

                error != null -> {
                    Text("에러: $error")
                }

                else -> {
                    ProductCard(
                        products = viewModel.products,
                        onProductClick = { product ->
                            viewModel.openProductDetail(product)
                        }
                    )
                }
            }
        }

        // ProductDetail 오버레이
        val productDetail = viewModel.currentDetail
        val detailVisible = productDetail != null

        if (detailVisible) {
            // 배경 오버레이 (클릭 시 닫기)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        viewModel.closeProductDetail()
                    }
            )

            AnimatedVisibility(
                visible = detailVisible,
                enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
            ) {
                ProductDetail(
                    image = productDetail.productImagePath,
                    title = productDetail.name,
                    desc = productDetail.description,
                    nut = productDetail.nutritionInfo,
                    onClose = viewModel::closeProductDetail,
                    onAddToCart = {
                        cartViewModel.addProduct(productDetail)
                    },
                    onGoCart = {
                        navController.navigate("addOrCart")
                    }
                )
            }
        }
    }
}

