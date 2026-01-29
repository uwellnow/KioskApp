package com.app.stronglife.data.model

import com.google.gson.annotations.SerializedName

data class ProductsByPurposeResponse(
    @SerializedName("storeName") val storeName: String,
    @SerializedName("purposes") val purposes: List<PurposeGroup>
)

data class PurposeGroup(
    @SerializedName("purpose") val purpose: String,
    @SerializedName("products") val products: List<Product>
)
