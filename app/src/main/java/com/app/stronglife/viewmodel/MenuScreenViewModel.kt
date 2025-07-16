package com.app.stronglife.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.app.stronglife.data.model.Product
import androidx.compose.runtime.State

class MenuScreenViewModel : ViewModel() {
    private val _previewingProduct = mutableStateOf<Product?>(null)
    val previewingProduct: State<Product?> = _previewingProduct

    fun selectProduct(product: Product) {
        _previewingProduct.value = product
    }

    fun clearSelection() {
        _previewingProduct.value = null
    }

}