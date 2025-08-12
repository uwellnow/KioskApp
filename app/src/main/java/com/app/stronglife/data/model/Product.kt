package com.app.stronglife.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    val name: String,
    val timing: String,
    val description: String,
    @SerializedName("nutrition_info") val nutritionInfo: String,
    @SerializedName("company_image_path") val companyImagePath: String,
    @SerializedName("product_image_path") val productImagePath: String
)