package com.app.stronglife.data.model

import com.google.gson.annotations.SerializedName

data class Stock(
    val productId: Int,
    val productName: String,
    val productTime: String,
    val productDescription: String,
    val productCount: Int,
    val updatedAddTime: String,
    val manager: String,
    val productStatus: String,
    val storeName: String
)
