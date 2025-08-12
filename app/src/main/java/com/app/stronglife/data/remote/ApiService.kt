package com.app.stronglife.data.remote

import com.app.stronglife.data.model.Product
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import com.app.stronglife.data.model.KioskLogPayload
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>

    @GET("products/{product_id}/company-image")
    suspend fun getCompanyImage(
        @Path("product_id") productId: Int,
        @Header("x-api-key") apiKey: String
    ): ResponseBody

    @GET("products/{product_id}/product-image")
    suspend fun getProductImage(
        @Path("product_id") productId: Int,
        @Header("x-api-key") apiKey: String
    ): ResponseBody


    @POST("log/kiosk")
    suspend fun postKioskLog(
        @Header("x-api-key") apiKey: String,
        @Body payload: KioskLogPayload
    ): Response<ResponseBody> // 서버가 "string" 형태를 돌려준다 했으니 ResponseBody로 받음
}