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
import com.app.stronglife.data.remote.KioskLogger
import com.app.stronglife.data.remote.RetrofitClient
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import com.app.stronglife.util.LanguageManager

@Composable
fun PickMenuScreen(
    viewModel: ProductViewModel,
    cartViewModel: CartViewModel = viewModel(),
    navController: NavController = rememberNavController(),
    apiKey: String = "",
    purpose: String,
    languageManager: LanguageManager
) {
    val gs805ViewModel: Gs805ViewModel = viewModel()
    var machineError by remember { mutableStateOf<MachineError?>(null) }
    val scope = rememberCoroutineScope()

    val kioskLogger = remember(apiKey) {
        if (apiKey.isNotEmpty()) {
            KioskLogger(
                apiKey = apiKey,
                service = RetrofitClient.api,
                externalScope = scope,
                machineId = apiKey.toLongOrNull() ?: 0L,
                storeName = "스트롱라이프 GFC점"
            )
        } else {
            null
        }
    }

    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage
    val currentDetail = viewModel.currentDetail

    // 목적별 제품 로드
    LaunchedEffect(purpose) {
        if (apiKey.isNotEmpty()) {
            viewModel.setApiKey(apiKey)
        }
        viewModel.fetchProductsByPurpose()
        viewModel.selectPurpose(purpose)
        if (apiKey.isNotEmpty()) {
            viewModel.fetchStocks(forceRefresh = true)
        } else {
            println("PickMenuScreen: API 키가 없어 재고 정보를 로드할 수 없습니다.")
        }

        val wasSerialRunning = gs805ViewModel.isSerialRunning()
        kioskLogger?.logEvent("PickMenuScreen: wasSerialRunning=$wasSerialRunning", false)
        
        val serialOk = if (wasSerialRunning) {
            true
        } else {
            val started = gs805ViewModel.startSerial()
            kioskLogger?.logEvent("PickMenuScreen: startSerial()=$started", !started)
            started
        }
        
        if (serialOk) {
            try {
                if (!wasSerialRunning) {
                    delay(500)
                }
                kioskLogger?.logEvent("PickMenuScreen: Calling queryErrorCode()", false)
                val queryResult = gs805ViewModel.queryErrorCode()
                kioskLogger?.logEvent("PickMenuScreen: queryErrorCode() completed, responseHex=${queryResult.responseHex}", queryResult.responseHex == null)
                val errorCode = queryResult.businessResult

                kioskLogger?.logEvent(
                    detail = "PickMenuScreen QueryErrorCode: errorCode=0x${errorCode.toString(16)}, responseHex=${queryResult.responseHex}",
                    isError = (errorCode != 0x00 && errorCode != -1),
                    commandHex = queryResult.sentHex,
                    responseHex = queryResult.responseHex
                )
                
                machineError = when {
                    errorCode == 0x00 -> null
                    errorCode == 0x01 -> {
                        kioskLogger?.logEvent("PickMenuScreen MachineError: WaterShortage (0x01)", true)
                        MachineError.WaterShortage
                    }
                    errorCode == 0x02 -> {
                        kioskLogger?.logEvent("PickMenuScreen MachineError: CupShortage (0x02)", true)
                        MachineError.CupShortage
                    }
                    errorCode == -1 -> {
                        kioskLogger?.logEvent("PickMenuScreen MachineError: SerialError (response null or -1)", true)
                        MachineError.SerialError
                    }
                    queryResult.responseHex == null -> {
                        kioskLogger?.logEvent("PickMenuScreen MachineError: SerialError (responseHex null)", true)
                        MachineError.SerialError
                    }
                    else -> {
                        kioskLogger?.logEvent("PickMenuScreen MachineError: OtherError (0x${errorCode.toString(16)})", true)
                        MachineError.OtherError(errorCode)
                    }
                }
            } catch (e: Exception) {
                kioskLogger?.logEvent("PickMenuScreen MachineError: Exception - ${e.javaClass.simpleName}: ${e.message}", true)
                machineError = MachineError.SerialError
            } finally {
                if (!wasSerialRunning) {
                    gs805ViewModel.stopSerial()
                }
            }
        } else {
            kioskLogger?.logEvent("PickMenuScreen: Serial connection failed (ignored)", false)
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
            TopBar(2, listOf("섭취목적 선택",
                "${purpose}>메뉴선택"
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
            machineError is MachineError.WaterShortage -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorBox("물 부족", "물이 부족합니다. 관리자에게 문의하세요.") {
                        machineError = null
                        navController.navigate("menu") { popUpTo(0) }
                    }
                }
            }
            machineError is MachineError.CupShortage -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorBox("컵 부족", "컵이 부족합니다. 관리자에게 문의하세요.") {
                        machineError = null
                        navController.navigate("menu") { popUpTo(0) }
                    }
                }
            }
            machineError is MachineError.OtherError -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorBox("기기 오류", "기기 오류가 발생했습니다. (에러 코드: 0x${(machineError as MachineError.OtherError).code.toString(16)})") {
                        machineError = null
                        navController.navigate("menu") { popUpTo(0) }
                    }
                }
            }
            machineError is MachineError.SerialError -> {
            }
            cupShortage -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
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
