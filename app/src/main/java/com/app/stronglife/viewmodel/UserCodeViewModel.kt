package com.app.stronglife.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stronglife.data.model.LoginResponse
import com.app.stronglife.data.model.ProductPurchaseRequest
import com.app.stronglife.data.model.UserLoginRequest
import com.app.stronglife.data.remote.ApiService
import kotlinx.coroutines.launch
import retrofit2.Response

class UserCodeViewModel(
    private val api: ApiService
) : ViewModel() {

    companion object {
        private var instance: UserCodeViewModel? = null
        
        fun getInstance(api: ApiService): UserCodeViewModel {
            if (instance == null) {
                instance = UserCodeViewModel(api)
            }
            return instance!!
        }
    }

    fun sendApiKey(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = api.postApiKey(ApiService.ApiKeyRequest(apiKey))
                Log.d("UserCodeViewModel", "Server Response: $response")
            } catch (e: Exception) {
                Log.e("UserCodeViewModel", "Failed to send API Key", e)
            }
        }
    }

    var userCode = mutableStateOf("")
    var paymentMethodId = mutableStateOf(0)
    var loginResponse = mutableStateOf<LoginResponse?>(null)
        private set
    var errorMessage = mutableStateOf<String?>(null)

    fun addDigit(digit: String) {
        userCode.value += digit
    }

    fun removeLast() {
        if (userCode.value.isNotEmpty()) {
            userCode.value = userCode.value.dropLast(1)
        }
    }

    fun clear() {
        userCode.value = ""
    }

    fun setPaymentMethodId(id: Int) {
        paymentMethodId.value = id
    }

    fun fetchUser(apiKey: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response: Response<LoginResponse> = api.postUserLogin(
                    apiKey = apiKey,
                    request = UserLoginRequest(userCode.value, paymentMethodId.value)
                )
                if (response.isSuccessful) {
                    loginResponse.value = response.body()
                    onResult(true)
                } else {
                    errorMessage.value = "조회 실패 (${response.code()})"
                    onResult(false)
                }
            } catch (e: Exception) {
                errorMessage.value = e.message
                onResult(false)
            }
        }
    }

    fun purchaseProductByOrder(
        apiKey: String, 
        orderNumber: String, 
        productIds: List<Int>, 
        productCounts: List<Int>,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                Log.d("UserCodeViewModel", "Purchase request - API Key: $apiKey, Order Number: $orderNumber")
                Log.d("UserCodeViewModel", "Product IDs: $productIds, Product Counts: $productCounts")
                
                val request = ProductPurchaseRequest(productIds, productCounts)
                val response: Response<okhttp3.ResponseBody> = api.postPurchaseProductByOrder(
                    apiKey = apiKey,
                    orderNumber = orderNumber,
                    body = request
                )
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    Log.d("UserCodeViewModel", "Purchase successful: $responseBody")
                    onResult(true)
                } else {
                    Log.e("UserCodeViewModel", "Purchase failed with code: ${response.code()}")
                    Log.e("UserCodeViewModel", "Error body: ${response.errorBody()?.string()}")
                    errorMessage.value = "구매 실패 (${response.code()})"
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("UserCodeViewModel", "Purchase exception: ${e.message}", e)
                errorMessage.value = e.message
                onResult(false)
            }
        }
    }

    fun setLoginResponse(response: LoginResponse) {
        loginResponse.value = response
    }
}
