package com.app.stronglife.data.model

data class User (
    val id: Int,
    val name: String,
    val remain: Int, // 남은 잔 수
)

data class UserLoginRequest(
    val userCode: String
)

data class Membership(
    val id: Int,
    val barcode: String,
    val membership_name: String,
    val remain_count: Int,
    val total_count: Int
)

data class LoginResponse(
    val id: Int,
    val user_code: String,
    val name: String,
    val age: Int,
    val gender: String,
    val membership: Membership
)

data class UserPurchase(
    val productId: List<Int>,
    val productCount: List<Int>,
    val userId: String,
    val userCode: String,
)