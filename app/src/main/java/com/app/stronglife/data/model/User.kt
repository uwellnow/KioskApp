package com.app.stronglife.data.model

data class User (
    val id: Int,
    val name: String,
    val remain: Int, // 남은 잔 수
)

data class UserLoginRequest(
    val userCode: String
)

data class LoginResponse(
    val id: Int,
    val name: String,
    val userCode: String,
    val age: Int,
    val gender: String,
    val remainCount: Int,
)

data class UserPurchase(
    val productId: List<Int>,
    val productCount: List<Int>,
    val userId: String,
    val userCode: String,

)