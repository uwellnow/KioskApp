package com.app.stronglife.data.remote

import com.app.stronglife.data.model.LoginResponse
import com.app.stronglife.data.model.Product
import com.app.stronglife.data.model.UserLoginRequest
import com.app.stronglife.data.model.UserPurchase
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>

    @POST("user/login")
    suspend fun postUserLogin(
        @Header("x-api-key") apiKey: String,
        @Body body: UserLoginRequest
    ) : ResponseBody

    @POST("user/purchase/product")
    suspend fun postPurchaseProduct(
        @Header("x-api-key") apiKey: String,
        @Body body: UserPurchase
    ) : ResponseBody
}