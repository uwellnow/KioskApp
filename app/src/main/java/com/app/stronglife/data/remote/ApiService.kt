package com.app.stronglife.data.remote

import com.app.stronglife.data.model.Product
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

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
}