package com.app.stronglife.ui.screen.menuScreen

import CartViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.mock.sampleProducts
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.viewmodel.MenuScreenViewModel

@Composable
fun MenuScreen(
    menuViewModel: MenuScreenViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    navController: NavController = rememberNavController()
) {
    val product by menuViewModel.previewingProduct

    val density = LocalDensity.current
    val spacertoDp = with(density) {80f.toDp()}
    Box {
        Column (
            modifier = Modifier.alpha(if (product != null) 0.3f else 1f)
        ){
            TopBar(step = 2, listOf("섭취시점 선택", "메뉴선택"), navController = navController)
            Spacer(modifier = Modifier.height(spacertoDp))
            ProductCard(sampleProducts, onProductClick = menuViewModel::selectProduct)
        }

        product?.let { product ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = spacertoDp),
                contentAlignment = Alignment.Center
            ) {
                ProductDetail(
                    image = product.imageUrl2,
                    title = product.title,
                    nut = product.nutrition,
                    onClose = menuViewModel::clearSelection,
                    onAddToCart = { cartViewModel.addProduct(product)},
                    onGoCart = {navController.navigate("addOrCart")}
                )
            }
        }
    }


}


@Composable
@Preview(
    name = "1920x1080 Landscape",
    showBackground = true,
    device = "spec:width=1920px,height=1080px,dpi=81"
)
fun MenuScreenPreview() {
    MenuScreen()
}