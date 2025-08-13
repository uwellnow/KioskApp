package com.app.stronglife.data.remote

import com.app.stronglife.data.model.LoginResponse
import com.app.stronglife.data.model.Product
import com.app.stronglife.data.model.UserLoginRequest
import com.app.stronglife.data.model.UserPurchase
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import com.app.stronglife.data.model.KioskLogPayload
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    data class ApiKeyRequest(
        val api_key: String
    )

    @POST("kiosk")
    suspend fun postApiKey(
        @Body request: ApiKeyRequest
    ): ResponseBody

    @GET("products")
    suspend fun getProducts(): List<Product>

    @POST("user/login")
    suspend fun postUserLogin(
        @Header("x-api-key") apiKey: String,
        @Body request: UserLoginRequest
    ) : retrofit2.Response<LoginResponse>


    @POST("user/purchase/product")
    suspend fun postPurchaseProduct(
        @Header("x-api-key") apiKey: String,
        @Body body: UserPurchase
    ) : ResponseBody
  

    @POST("log/kiosk")
    suspend fun postKioskLog(
        @Header("x-api-key") apiKey: String,
        @Body payload: KioskLogPayload
    ): Response<ResponseBody> 

}