package com.app.stronglife.data.remote

import retrofit2.http.GET

interface ApiService {
    @GET("/stocks")
    suspend fun getStocks(): List<Stock>
}