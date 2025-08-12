package com.app.stronglife.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.app.stronglife.data.model.Product
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.app.stronglife.data.remote.RetrofitClient
import kotlinx.coroutines.launch


class MenuScreenViewModel : ViewModel() {
    // 전체 상품 목록
    private val _products = mutableStateOf<List<Product>>(emptyList())
    val products: State<List<Product>> = _products

    // 현재 상세보기 중인 상품 (null이면 ProductDetail 안 보임)
    private val _currentDetail = mutableStateOf<Product?>(null)
    val currentDetail: State<Product?> = _currentDetail

    // 로딩/에러 상태
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage


    fun openProductDetail(product: Product) {
        _currentDetail.value = product
    }

    fun closeProductDetail() {
        _currentDetail.value = null
    }

    fun fetchProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = RetrofitClient.api.getProducts()
                _products.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

}