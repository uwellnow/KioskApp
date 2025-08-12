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
    private val apiKey: String
) : ViewModel() {

    var products by mutableStateOf<List<Product>>(emptyList())
        private set

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            try {
                val productList = apiService.getProducts()
                val updatedList = productList.map { product ->
                    val bytes = apiService.getCompanyImage(product.id, apiKey).bytes()
                    val base64 = Base64.encodeToString(bytes, Base64.DEFAULT)
                    product.copy(companyURL = "data:image/png;base64,$base64")
                }
                products = updatedList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
