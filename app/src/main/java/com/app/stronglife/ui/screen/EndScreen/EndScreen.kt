package com.app.stronglife.ui.screen.EndScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.app.stronglife.R
import com.app.stronglife.data.remote.KioskLogger
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.ui.component.ErrorBox
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.cardPayGray
import com.app.stronglife.ui.theme.midGray
import com.app.stronglife.viewmodel.Gs805ViewModel
import com.app.stronglife.viewmodel.Gs805ViewModel.MachineEvent
import com.app.stronglife.viewmodel.ProductViewModel
import com.app.stronglife.viewmodel.ProductViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EndScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    vm: Gs805ViewModel = viewModel(),
    apiKey: String
) {

    val cartItems = cartViewModel.cartItems.value
    val products = productViewModel.products


    Finish(navController, cartViewModel)

    LaunchedEffect(Unit) {
        if (products.isEmpty()) {
            productViewModel.fetchProducts()
        }
    }


    // KioskLogger 인스턴스 생성 (1번 코드 방식)
    val scope = rememberCoroutineScope()
    val kioskLogger = remember {
        KioskLogger(
            apiKey = apiKey,
            service = RetrofitClient.api,
            externalScope = scope,
            machineId = apiKey.toLong(),
            storeName = "스트롱라이프 GFC점" // 실제 매장명으로 교체 필요
        )
    }

    LaunchedEffect(cartItems, products) {
        kioskLogger.logEvent(
            detail = "Cart items count: ${cartItems.size}, Products count: ${products.size}",
            isError = false
        )

        cartItems.forEach { cartItem ->
            val product = products.find { it.id == cartItem.product.id }
            if (product != null) {
                kioskLogger.logEvent(
                    detail = "Cart item: ${product.name} (ID: ${product.id}, Quantity: ${cartItem.quantity})",
                    isError = false
                )
                kioskLogger.logEvent(
                    detail = "Recipe slots for ${product.name}: ${product.recipeSlots}",
                    isError = false
                )
            } else {
                kioskLogger.logEvent(
                    detail = "Product not found for cart item ID: ${cartItem.product.id}",
                    isError = true
                )
            }
        }
    }


    // 요청-응답 시퀀스 실행 및 로깅
    LaunchedEffect(Unit) {
        // 1. 시리얼 연결
        val serialOk = vm.startSerial()
        kioskLogger.logEvent(
            detail = "SerialStart",
            isError = !serialOk,
            responseHex = if (serialOk) "Connection successful" else null
        )
        if (!serialOk) return@LaunchedEffect

        delay(100L)

        // 2. 상태 확인
        val queryResult = vm.queryErrorCode()
        kioskLogger.logEvent(
            detail = "QueryErrorCode",
            isError = (queryResult.responseHex == null || queryResult.businessResult == -1),
            commandHex = queryResult.sentHex,
            responseHex = queryResult.responseHex
        )

        delay(100L)

        // 3. 레시피 저장
        val slots = listOf(50 to 200, 0 to 0, 0 to 0, 0 to 0, 0 to 0, 0 to 0, 0 to 0, 0 to 0)
        val recipeResult = vm.saveRecipe3(0x11, slots)
        kioskLogger.logEvent(
            detail = "SaveRecipe",
            isError = !recipeResult.businessResult,
            commandHex = recipeResult.sentHex,
            responseHex = recipeResult.responseHex
        )

        delay(100L)

        // 4. 제조 시작
        val makeResult = vm.makeDrinkNow(0x11, localOrCmd = 0x02)
        kioskLogger.logEvent(
            detail = "MakeDrink",
            isError = !makeResult.businessResult,
            commandHex = makeResult.sentHex,
            responseHex = makeResult.responseHex
        )
    }

    // 비동기 이벤트 수신 및 로깅
    LaunchedEffect(Unit) {
        vm.events.collectLatest { ev ->
            when (ev) {
                is MachineEvent.RawDataReceived -> {
                    // RawData는 디버깅용이므로 errorDetail을 활용해 명시
                    kioskLogger.logEvent(detail = "RawDataReceived", isError = false, responseHex = ev.hex)
                }
                is MachineEvent.DrinkCompleted -> {
                    kioskLogger.logEvent(detail = "Event: DrinkCompleted", isError = false, responseHex = ev.hex)
                    delay(300)
                    navController.navigate("hello")
                }
                is MachineEvent.CupDropped -> {
                    kioskLogger.logEvent(detail = "Event: CupDropped", isError = false, responseHex = ev.hex)
                }
                is MachineEvent.Offline -> {
                    val message = "Offline cmd=0x${ev.cmd.toString(16)}"
                    kioskLogger.logEvent(detail = message, isError = true)
                }
                is MachineEvent.ErrorCode -> {
                    val message = if (ev.code == -1) {
                        "SerialCommunicationError"
                    } else {
                        "MachineErrorCode=0x${ev.code.toString(16)}"
                    }
                    kioskLogger.logEvent(detail = message, isError = true)
                }
            }
        }
    }
}// private suspend fun sendLog(...)  <- 이 함수는 삭제합니다.

