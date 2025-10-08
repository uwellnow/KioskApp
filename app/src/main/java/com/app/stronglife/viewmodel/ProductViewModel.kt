package com.app.stronglife.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stronglife.data.model.Product
import com.app.stronglife.data.model.Stock
import com.app.stronglife.data.remote.ApiService
import kotlinx.coroutines.launch
import android.util.Base64

class ProductViewModel(
    private val apiService: ApiService,
) : ViewModel() {

    private var apiKey: String = ""

    fun setApiKey(key: String) {
        apiKey = key
    }

    var selectedNutritionProduct by mutableStateOf<Product?>(null)
        private set

    fun openNutrition(product: Product) {
        selectedNutritionProduct = product
    }

    fun closeNutrition() {
        selectedNutritionProduct = null
    }

    var products by mutableStateOf<List<Product>>(emptyList())
        private set

    var stocks by mutableStateOf<List<Stock>>(emptyList())
        private set

    var currentDetail by mutableStateOf<Product?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun openProductDetail(product: Product) {
        currentDetail = product
    }

    fun closeProductDetail() {
        currentDetail = null
    }

    fun fetchProducts(forceRefresh: Boolean = false) {
        if (products.isNotEmpty() && !forceRefresh) {
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val result = apiService.getProducts()
                products = result.filter { it.id in 1..7 }
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun fetchStocks(forceRefresh: Boolean = false) {
        if (stocks.isNotEmpty() && !forceRefresh) {
            return
        }

        if (apiKey.isEmpty()) {
            println("API 키가 설정되지 않아 재고 정보를 가져올 수 없습니다.")
            return
        }

        viewModelScope.launch {
            try {
                println("재고 정보 API 호출 시작 - API 키: $apiKey")
                val result = apiService.getStocks(apiKey)
                stocks = result
                println("재고 정보 로드 완료: ${stocks.size}개 상품")
                if (stocks.isEmpty()) {
                    println("재고 정보가 비어있습니다. 서버에 재고 데이터가 없을 수 있습니다.")
                } else {
                    stocks.forEach { stock ->
                        println("상품 ID: ${stock.productId}, 재고: ${stock.productCount}")
                    }
                }
            } catch (e: Exception) {
                // 재고 정보 로딩 실패는 무시 (상품 목록은 정상 표시)
                println("Failed to fetch stocks: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun isProductSoldOut(productId: Int): Boolean {
        val isSoldOut = stocks.any { it.productId == productId && it.productCount == 0 }
        return isSoldOut
    }

}
