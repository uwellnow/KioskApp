package com.app.stronglife.data.model

import com.google.gson.annotations.SerializedName

data class SystemStatus(
    val id: Int,
    @SerializedName("api_key")
    val apiKey: String,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("status_type")
    val statusType: String,
    @SerializedName("created_at")
    val createdAt: String
)
