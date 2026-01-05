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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.R
import com.app.stronglife.ui.component.ErrorBox
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.background
import com.app.stronglife.viewmodel.ProductViewModel
import com.app.stronglife.viewmodel.Gs805ViewModel
import kotlinx.coroutines.delay

sealed class MachineError {
    object WaterShortage : MachineError()
    object CupShortage : MachineError()
    data class OtherError(val code: Int) : MachineError()
    object SerialError : MachineError()
}

@Composable
fun MenuScreen(
    viewModel: ProductViewModel,
    cartViewModel: CartViewModel = viewModel(),
    navController: NavController = rememberNavController(),
    apiKey: String = "",
    languageManager: com.app.stronglife.util.LanguageManager
) {
    val gs805ViewModel: Gs805ViewModel = viewModel()
    var machineError by remember { mutableStateOf<MachineError?>(null) }

    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage
    val currentDetail = viewModel.currentDetail

    LaunchedEffect(Unit) {
        if (apiKey.isNotEmpty()) {
            viewModel.setApiKey(apiKey)
        }
        viewModel.fetchProducts()
        if (apiKey.isNotEmpty()) {
            viewModel.fetchStocks(forceRefresh = true)
        } else {
            println("MenuScreen: API 키가 없어 재고 정보를 로드할 수 없습니다.")
        }

        val serialOk = gs805ViewModel.startSerial()
        if (serialOk) {
            try {
                val queryResult = gs805ViewModel.queryErrorCode(retries = 10)
                val errorCode = queryResult.businessResult
                
                machineError = when {
                    errorCode == 0x00 -> null
                    errorCode == 0x01 -> MachineError.WaterShortage
                    errorCode == 0x02 -> MachineError.CupShortage
                    errorCode == -1 -> MachineError.SerialError
                    queryResult.responseHex == null -> MachineError.SerialError
                    else -> MachineError.OtherError(errorCode)
                }
            } catch (e: Exception) {
                machineError = MachineError.SerialError
            } finally {
                gs805ViewModel.stopSerial()
            }
        } else {
            // 시리얼 연결 실패는 무시 (기기 없을 수도 있음)
        }
    }

    val cupShortage by remember(viewModel.stocks) {
        derivedStateOf { viewModel.stocks.any { it.productId == 100 && it.productCount == 0 } }
    }
    val waterShortage by remember(viewModel.stocks) {
        derivedStateOf { viewModel.stocks.any { it.productId == 101 && it.productCount == 0 } }
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
            TopBar(2, listOf(stringResource(R.string.top_1),
                stringResource(R.string.top_2)
            ), navController, cartViewModel = cartViewModel)
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
                        },
                        viewModel,
                        languageManager = languageManager
                    )
                }
            }
        }

        when {
            cupShortage -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White), // 뒷배경 흐리게
                    contentAlignment = Alignment.Center
                ) {
                    ErrorBox("컵 부족", "컵이 부족합니다. 관리자에게 문의해 주세요"){
                        navController.popBackStack()
                    }
                }
            }
            waterShortage -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorBox("물 부족", "물이 부족합니다. 관리자에게 문의해 주세요"){
                        navController.popBackStack()
                    }
                }
            }
        }

        // ProductDetail 오버레이
        val productDetail = viewModel.currentDetail
        val detailVisible = productDetail != null
        val langTag by languageManager.languageTag.collectAsState()

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
                    image = productDetail.productImagePath.ifBlank { "" },
                    title = if (langTag == "ko") productDetail.name else (if (productDetail.nameEng.isNotBlank()) productDetail.nameEng else productDetail.name),
                    desc = if (langTag == "ko") productDetail.description else (if (productDetail.descriptionEng.isNotBlank()) productDetail.descriptionEng else productDetail.description),
                    nut = productDetail.nutritionInfo,
                    nut_eng = productDetail.nutritionInfoEng,
                    isSoldOut = viewModel.isProductSoldOut(productDetail.id),
                    onClose = viewModel::closeProductDetail,
                    onAddToCart = {
                        if (!viewModel.isProductSoldOut(productDetail.id)) {
                            cartViewModel.addProduct(productDetail)
                        }
                    },
                    onGoCart = {
                        navController.navigate("addOrCart")
                    },
                    languageManager = languageManager
                )
            }
        }
    }
}

