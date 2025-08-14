package com.app.stronglife.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stronglife.data.model.LoginResponse
import com.app.stronglife.data.model.UserLoginRequest
import com.app.stronglife.data.remote.ApiService
import kotlinx.coroutines.launch
import retrofit2.Response

class UserCodeViewModel(
    private val api: ApiService
) : ViewModel() {

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

    fun fetchUser(apiKey: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response: Response<LoginResponse> = api.postUserLogin(
                    apiKey = apiKey,
                    request = UserLoginRequest(userCode.value)
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

    fun setLoginResponse(response: LoginResponse) {
        loginResponse.value = response
    }
}
