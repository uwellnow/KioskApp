package com.app.stronglife.ui.screen.menuScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
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
    val spacertoDp = with(density) {80f.toDp()}

    Box {
        Column (
            modifier = Modifier
                .alpha(if (currentDetail != null) 0.3f else 1f)
                .fillMaxSize()
                .background(background)
        ){
            TopBar(step = 2, listOf("섭취시점 선택", "메뉴선택"), navController = navController)
            Spacer(modifier = Modifier.height(spacertoDp))
            when {
                isLoading -> {
                    Text("로딩 중...")
                }
                error != null -> {
                    Text("에러: $error")
                }
                else -> {
                    // 가로 스크롤 전체 상품 목록
                    ProductCard(
                        products = viewModel.products,
                        onProductClick = { product ->
                            viewModel.openProductDetail(product)
                        }
                    )
                }
        }

        viewModel.currentDetail?.let { product ->
            ProductDetail(
                image = product.productImagePath,
                title = product.name,
                nut = product.nutritionInfo,
                onClose = viewModel::closeProductDetail,
                onAddToCart = {
                    cartViewModel.addProduct(product) // 장바구니에 추가
                },
                onGoCart = {
                    navController.navigate("addOrCart")
                }
            )
        }
    }
}}

