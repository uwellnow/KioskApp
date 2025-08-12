package com.app.stronglife.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stronglife.data.model.Product
import com.app.stronglife.data.remote.ApiService
import kotlinx.coroutines.launch
import android.util.Base64

class ProductViewModel(
    private val apiService: ApiService,
) : ViewModel() {

    var products by mutableStateOf<List<Product>>(emptyList())
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

    fun fetchProducts() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                products = apiService.getProducts()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}
