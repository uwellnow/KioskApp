package com.app.stronglife.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stronglife.data.model.LoginResponse
import com.app.stronglife.data.model.ProductPurchaseRequest
import com.app.stronglife.data.model.CouponPurchaseRequest
import com.app.stronglife.data.model.UserLoginRequest
import com.app.stronglife.data.model.PhoneLoginRequest
import com.app.stronglife.data.model.ProductPurchaseResponse
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

    fun sendApiKey(apiKey: String, onResult: (Boolean, Boolean?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            try {
                val response = api.postApiKey(ApiService.ApiKeyRequest(apiKey))
                if (response.isSuccessful) {
                    val kioskResponse = response.body()
                    val isPicked = kioskResponse?.isPicked
                    Log.d("api key", "API Key 등록 성공: ${response.body()}, is_picked: $isPicked")
                    Log.d("api key", "isPicked 값 확인: $isPicked, 타입: ${isPicked?.javaClass?.simpleName}")
                    errorState.value = UiError.None
                    onResult(true, isPicked)
                } else {
                    Log.e("api key", "API Key 전송 실패 - 코드: ${response.code()}")
                    val error = when (response.code()) {
                        401 -> UiError.Generic("유효하지 않은 API 키입니다. 다시 확인해주세요.")
                        else -> UiError.Generic("API 키 검증 실패 (${response.code()})")
                    }
                    errorState.value = error
                    onResult(false, null)
                }
            } catch (e: Exception) {
                Log.e("api key", "API Key 전송 오류", e)
                errorState.value = UiError.Exception(e)
                onResult(false, null)
            }
        }
    }

    var userCode = mutableStateOf("")
    var paymentMethodId = mutableStateOf(1)
    var loginResponse = mutableStateOf<LoginResponse?>(null)
        private set
    var selectedPaymentMethod = mutableStateOf<String?>(null)  // "phone" 또는 "order"
    var errorMessage = mutableStateOf<String?>(null)
    var is404Error = mutableStateOf(false)
    var is400Error = mutableStateOf(false)
    var is409Error = mutableStateOf(false)

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

    fun resetAll() {
        userCode.value = ""
        loginResponse.value = null
        selectedPaymentMethod.value = null
        errorMessage.value = null
        is404Error.value = false
        is400Error.value = false
        is409Error.value = false
        errorState.value = UiError.None
    }

    fun clear404Error() {
        errorState.value = UiError.None
        loginResponse.value = null
        errorMessage.value = null
    }

    fun clearPurchaseError() {
        errorState.value = UiError.None
        errorMessage.value = null
    }

    fun setPaymentMethodId(id: Int) {
        paymentMethodId.value = id
    }

    fun fetchUser(apiKey: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("UserCodeViewModel", "Fetch user request - API Key: $apiKey, User Code: ${userCode.value}")
                val response: Response<LoginResponse> = api.postUserLogin(
                    apiKey = apiKey,
                    request = UserLoginRequest(userCode.value, paymentMethodId.value)
                )
                if (response.isSuccessful) {
                    loginResponse.value = response.body()
                    errorState.value = UiError.None
                    onResult(true)
                } else {
                    Log.e("UserCodeViewModel", "Fetch user failed with code: ${response.code()}")
                    Log.e("UserCodeViewModel", "Error body: ${response.errorBody()?.string()}")
                    
                    val error = when (response.code()) {
                        400 -> {
                            val errorBody = response.errorBody()?.string() ?: ""
                            when {
                                errorBody.contains("이미 사용 완료된 주문번호") -> UiError.Generic("이미 사용 완료된 주문번호입니다")
                                errorBody.contains("보유 잔수가 부족") -> UiError.InsufficientBalance
                                else -> UiError.Generic("잔여 잔 수가 부족합니다")
                            }
                        }
                        401 -> UiError.Generic("인증 실패 (${response.code()})")
                        404 -> UiError.NotFound
                        409 -> UiError.OutOfStock
                        else -> UiError.Generic("조회 실패 (${response.code()})")
                    }
                    errorState.value = error
                    Log.d("UserCodeViewModel", "Error state set to: $error")
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("UserCodeViewModel", "Fetch user exception: ${e.message}", e)
                errorState.value = UiError.Exception(e)
                onResult(false)
            }
        }
    }

    sealed class UiError {
        object None : UiError()
        object NotFound : UiError()              // 404
        object InsufficientBalance : UiError()   // 400
        object OutOfStock : UiError()            // 409
        data class Generic(val message: String) : UiError()
        data class Exception(val throwable: Throwable) : UiError()
    }

    var errorState = mutableStateOf<UiError>(UiError.None)

    /** 결제 성공 시 서버에서 내려준 첫 주문 여부. EndScreen/Finish에서 읽은 뒤 clearLastPurchaseIsFirstOrder() 호출 권장 */
    var lastPurchaseIsFirstOrder = mutableStateOf<Boolean?>(null)



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
                val response: Response<ProductPurchaseResponse> = api.postPurchaseProductByOrder(
                    apiKey = apiKey,
                    orderNumber = orderNumber,
                    body = request
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("UserCodeViewModel", "Purchase successful: $body")
                    Log.d("UserCodeViewModel", "body is null: ${body == null}")
                    if (body != null) {
                        Log.d("UserCodeViewModel", "body.isFirstOrder=${body.isFirstOrder}")
                        lastPurchaseIsFirstOrder.value = body.isFirstOrder
                    } else {
                        Log.w("UserCodeViewModel", "Response body is null!")
                        lastPurchaseIsFirstOrder.value = null
                    }
                    Log.d("UserCodeViewModel", "lastPurchaseIsFirstOrder.value set to: ${lastPurchaseIsFirstOrder.value}")
                    errorState.value = UiError.None
                    onResult(true)
                } else {
                    Log.e("UserCodeViewModel", "Purchase failed with code: ${response.code()}")
                    Log.e("UserCodeViewModel", "Error body: ${response.errorBody()?.string()}")
                    
                    val error = when (response.code()) {
                        400 -> UiError.InsufficientBalance
                        401 -> UiError.Generic("인증 실패 (${response.code()})")
                        404 -> UiError.NotFound
                        409 -> UiError.OutOfStock
                        else -> UiError.Generic("조회 실패 (${response.code()})")
                    }
                    errorState.value = error
                    Log.d("UserCodeViewModel", "Error state set to: $error")
                    onResult(false)
                }
            } catch (e: Exception) {
                errorState.value = UiError.Exception(e)
                onResult(false)
            }
        }
    }

    fun setLoginResponse(response: LoginResponse) {
        loginResponse.value = response
    }

    fun purchaseByCoupon(
        apiKey: String,
        couponCode: String,
        productIds: List<Int>,
        productCounts: List<Int>,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                Log.d("UserCodeViewModel", "Coupon purchase request - API Key: $apiKey, Coupon Code: $couponCode")
                Log.d("UserCodeViewModel", "Product IDs: $productIds, Product Counts: $productCounts")
                
                val request = CouponPurchaseRequest(couponCode, productIds, productCounts)
                val response: Response<ProductPurchaseResponse> = api.postPurchaseByCoupon(
                    apiKey = apiKey,
                    body = request
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("UserCodeViewModel", "Coupon purchase successful: $body")
                    Log.d("UserCodeViewModel", "body is null: ${body == null}")
                    if (body != null) {
                        Log.d("UserCodeViewModel", "body.isFirstOrder=${body.isFirstOrder}")
                        lastPurchaseIsFirstOrder.value = body.isFirstOrder
                    } else {
                        Log.w("UserCodeViewModel", "Response body is null!")
                        lastPurchaseIsFirstOrder.value = null
                    }
                    Log.d("UserCodeViewModel", "lastPurchaseIsFirstOrder.value set to: ${lastPurchaseIsFirstOrder.value}")
                    errorState.value = UiError.None
                    onResult(true)
                } else {
                    errorState.value = when (response.code()) {
                        400 -> UiError.InsufficientBalance
                        404 -> UiError.NotFound
                        409 -> UiError.OutOfStock
                        else -> UiError.Generic("쿠폰 조회 실패 (${response.code()})")
                    }
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("UserCodeViewModel", "Coupon purchase exception: ${e.message}", e)
                errorMessage.value = e.message
                errorState.value = UiError.Exception(e)
                onResult(false)
            }
        }
    }

    fun loginByPhone(
        apiKey: String,
        phoneInput: String,  // 8자리 또는 전체
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                Log.d("UserCodeViewModel", "Phone login request - API Key: $apiKey, Phone Input: $phoneInput")
                val response: Response<LoginResponse> = api.postUserLoginByPhone(
                    apiKey = apiKey,
                    request = PhoneLoginRequest(phoneInput)
                )
                if (response.isSuccessful) {
                    loginResponse.value = response.body()
                    errorState.value = UiError.None
                    onResult(true)
                } else {
                    Log.e("UserCodeViewModel", "Phone login failed with code: ${response.code()}")
                    Log.e("UserCodeViewModel", "Error body: ${response.errorBody()?.string()}")
                    
                    val error = when (response.code()) {
                        404 -> UiError.NotFound  // 등록 회원 없음 / 유효 멤버십 없음
                        409 -> UiError.OutOfStock  // 복수 회원 매칭
                        401 -> UiError.Generic("인증 실패 (${response.code()})")
                        else -> UiError.Generic("로그인 실패 (${response.code()})")
                    }
                    errorState.value = error
                    Log.d("UserCodeViewModel", "Error state set to: $error")
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("UserCodeViewModel", "Phone login exception: ${e.message}", e)
                errorState.value = UiError.Exception(e)
                onResult(false)
            }
        }
    }

    fun purchaseProductByPhone(
        apiKey: String,
        phoneNumber: String,  // 8자리 또는 전체
        productIds: List<Int>,
        productCounts: List<Int>,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                Log.d("UserCodeViewModel", "Phone purchase request - API Key: $apiKey, Phone Number: $phoneNumber")
                Log.d("UserCodeViewModel", "Product IDs: $productIds, Product Counts: $productCounts")
                
                val request = ProductPurchaseRequest(productIds, productCounts)
                val response: Response<ProductPurchaseResponse> = api.postPurchaseProductByPhone(
                    apiKey = apiKey,
                    phoneNumber = phoneNumber,
                    body = request
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("UserCodeViewModel", "Phone purchase successful: $body")
                    Log.d("UserCodeViewModel", "body is null: ${body == null}")
                    if (body != null) {
                        Log.d("UserCodeViewModel", "body.isFirstOrder=${body.isFirstOrder}")
                        lastPurchaseIsFirstOrder.value = body.isFirstOrder
                    } else {
                        Log.w("UserCodeViewModel", "Response body is null!")
                        lastPurchaseIsFirstOrder.value = null
                    }
                    Log.d("UserCodeViewModel", "lastPurchaseIsFirstOrder.value set to: ${lastPurchaseIsFirstOrder.value}")
                    errorState.value = UiError.None
                    onResult(true)
                } else {
                    Log.e("UserCodeViewModel", "Phone purchase failed with code: ${response.code()}")
                    Log.e("UserCodeViewModel", "Error body: ${response.errorBody()?.string()}")
                    
                    val error = when (response.code()) {
                        400 -> UiError.InsufficientBalance  // 잔여 부족 / 만료 멤버십 등
                        404 -> UiError.NotFound  // 회원/멤버십 없음
                        409 -> UiError.OutOfStock  // 복수 회원 매칭 or 중복 결제(5초 이내)
                        401 -> UiError.Generic("인증 실패 (${response.code()})")
                        else -> UiError.Generic("결제 실패 (${response.code()})")
                    }
                    errorState.value = error
                    Log.d("UserCodeViewModel", "Error state set to: $error")
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("UserCodeViewModel", "Phone purchase exception: ${e.message}", e)
                errorState.value = UiError.Exception(e)
                onResult(false)
            }
        }
    }

    /** EndScreen/Finish에서 isFirstOrder 읽은 뒤 호출하여 다음 결제까지 초기화 */
    fun clearLastPurchaseIsFirstOrder() {
        lastPurchaseIsFirstOrder.value = null
    }
}
