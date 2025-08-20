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
                products = result
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

        viewModelScope.launch {
            try {
                val result = apiService.getStocks()
                stocks = result
            } catch (e: Exception) {
                // 재고 정보 로딩 실패는 무시 (상품 목록은 정상 표시)
                println("Failed to fetch stocks: ${e.message}")
            }
        }
    }

    // 특정 상품이 품절인지 확인하는 함수
    fun isProductSoldOut(productId: Int): Boolean {
        return stocks.any { it.productId == productId && it.productCount == 0 }
    }
}
