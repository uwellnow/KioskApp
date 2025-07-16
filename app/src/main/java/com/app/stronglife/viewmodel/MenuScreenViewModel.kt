package com.app.stronglife.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.app.stronglife.data.model.Product
import androidx.compose.runtime.State

class MenuScreenViewModel : ViewModel() {
    private val _selectedProduct = mutableStateOf<Product?>(null)
    val selectedProduct : State<Product?> = _selectedProduct

    fun selectProduct(product: Product) {
        _selectedProduct.value = product
    }

    fun clearSelection() {
        _selectedProduct.value = null
    }
}