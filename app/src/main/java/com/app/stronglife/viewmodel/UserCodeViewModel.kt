package com.app.stronglife.viewmodel

import android.R.attr.apiKey
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stronglife.data.model.LoginResponse
import com.app.stronglife.data.model.UserLoginRequest
import com.app.stronglife.data.remote.ApiService
import kotlinx.coroutines.launch

class UserCodeViewModel (
    private val api: ApiService
) : ViewModel() {
    var userCode = mutableStateOf("")
    var loginResponse = mutableStateOf<LoginResponse?>(null)
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

    fun requestLogin(apiKey: String) {
        if (userCode.value.isEmpty()) return

        viewModelScope.launch {
            try {
                val request = UserLoginRequest(userCode = userCode.value)
                val response = api.postUserLogin(apiKey, request)

                Log.d("UserCodeViewModel", "POST 요청 성공 - 응답: $response")

                loginResponse.value = response
                errorMessage.value = null

            } catch (e: Exception) {
                errorMessage.value = e.message
            }
        }
    }
}