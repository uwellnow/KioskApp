package com.app.stronglife.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.app.stronglife.data.model.Product
import androidx.compose.runtime.State

class MenuScreenViewModel : ViewModel() {
    private val _previewingProduct = mutableStateOf<Product?>(null)
    val previewingProduct: State<Product?> = _previewingProduct

    // 장바구니
    private val _cart = mutableStateOf<List<Product>>(emptyList())
    val cart: State<List<Product>> = _cart

    fun selectProduct(product: Product) {
        _previewingProduct.value = product
    }

    fun clearSelection() {
        _previewingProduct.value = null
    }

    fun addToCart(product: Product) {
        _cart.value = _cart.value + product
        _previewingProduct.value = null
    }
}