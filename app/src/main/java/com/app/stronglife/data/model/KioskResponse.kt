package com.app.stronglife.data.model

import com.google.gson.annotations.SerializedName

data class KioskResponse(
    val message: String? = null,
    @SerializedName("api_key") val apiKey: String? = null,
    @SerializedName("is_picked") val isPicked: Boolean
)
