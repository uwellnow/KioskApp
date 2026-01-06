package com.app.stronglife.data.remote

import com.app.stronglife.data.model.LoginResponse
import com.app.stronglife.data.model.Product
import com.app.stronglife.data.model.ProductPurchaseRequest
import com.app.stronglife.data.model.CouponPurchaseRequest
import com.app.stronglife.data.model.UserLoginRequest
import com.app.stronglife.data.model.UserPurchase
import com.app.stronglife.data.model.Stock
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import com.app.stronglife.data.model.KioskLogPayload
import com.app.stronglife.data.model.SurveyRequest
import com.app.stronglife.data.model.SystemStatus
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    data class ApiKeyRequest(
        val api_key: String
    )

    @POST("kiosk")
    suspend fun postApiKey(
        @Body request: ApiKeyRequest
    ): Response<ResponseBody>

    @GET("products")
    suspend fun getProducts(
        @Header("x-api-key") apiKey: String
    ): List<Product>

    @GET("stocks/kiosk")
    suspend fun getStocks(
        @Header("x-api-key") apiKey: String
    ): List<Stock>

    @POST("user/login")
    suspend fun postUserLogin(
        @Header("x-api-key") apiKey: String,
        @Body request: UserLoginRequest
    ) : retrofit2.Response<LoginResponse>


    @POST("user/purchase/product")
    suspend fun postPurchaseProduct(
        @Header("x-api-key") apiKey: String,
        @Body body: UserPurchase
    ) : retrofit2.Response<okhttp3.ResponseBody>

    @POST("user/purchase/product-by-order")
    suspend fun postPurchaseProductByOrder(
        @Header("x-api-key") apiKey: String,
        @Query("orderNumber") orderNumber: String,
        @Body body: ProductPurchaseRequest
    ) : retrofit2.Response<okhttp3.ResponseBody>

    @POST("user/purchase/product-by-coupon")
    suspend fun postPurchaseByCoupon(
        @Header("x-api-key") apiKey: String,
        @Body body: CouponPurchaseRequest
    ) : retrofit2.Response<okhttp3.ResponseBody>
  

    @POST("log/kiosk")
    suspend fun postKioskLog(
        @Header("x-api-key") apiKey: String,
        @Body payload: KioskLogPayload
    ): Response<ResponseBody>


    @GET("system-status/poll")
    suspend fun pollSystemStatus(
        @Header("x-api-key") apiKey: String,
        @Query("status_type") statusType: String?,
        @Query("since") since:String?,
        @Query("timeout") timeout: Int?,
    ): Response<List<SystemStatus>>

    @POST("survey/kiosk")
    suspend fun submitSurvey(
        @Header("x-api-key") apiKey: String,
        @Header("userCode") userCode: String?,
        @Body request: SurveyRequest
    ): Response<Unit>

}