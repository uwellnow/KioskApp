package com.app.stronglife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.stronglife.data.remote.ApiService
import com.app.stronglife.viewmodel.ProductViewModel

class ProductViewModelFactory(
    private val apiService: ApiService,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}