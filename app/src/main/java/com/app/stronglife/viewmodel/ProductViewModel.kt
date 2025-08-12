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
                val productList = apiService.getProducts()

                val updatedList = productList.map { product ->
                    val combytes = apiService.getCompanyImage(product.id, apiKey).bytes()
                    val probytes = apiService.getProductImage(product.id, apiKey).bytes()
                    val combase64 = Base64.encodeToString(combytes, Base64.DEFAULT)
                    val probase64 = Base64.encodeToString(probytes, Base64.DEFAULT)
                    product.copy(
                        companyURL = "data:image/png;base64,$combase64",
                        productURL = "data:image/png;base64,$probase64"
                    )
                }

                products = updatedList

            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}
