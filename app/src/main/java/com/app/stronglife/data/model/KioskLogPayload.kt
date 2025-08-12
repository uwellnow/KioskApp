package com.app.stronglife.data.model

import com.google.gson.annotations.SerializedName

data class KioskLogPayload(
    @SerializedName("error_id")    val errorId: Long,
    val timestamp: String,                 // ISO-8601
    @SerializedName("machine_id")  val machineId: Long? = null,
    @SerializedName("store_name")  val storeName: String? = null,
    @SerializedName("error_type")  val errorType: String,     // "FRAME" / "ERROR"
    @SerializedName("error_detail")val errorDetail: String? = null,
    @SerializedName("command_sent")val commandSent: String? = null, // 보낸 프레임 Hex
    val response: String? = null,                                   // 수신 프레임 Hex
    @SerializedName("user_id")     val userId: String? = null,
    @SerializedName("product_id")  val productId: List<Long>? = null,
    @SerializedName("product_name")val productName: List<String>? = null
)
