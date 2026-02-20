package com.app.stronglife.data.model

data class User (
    val id: Int,
    val name: String,
    val remain: Int, // 남은 잔 수
)

data class UserLoginRequest(
    val userCode: String,
    val paymentMethodId: Int
)

data class Membership(
    val id: Int,
    val barcode: String,
    val membership_name: String,
    val remain_count: Int,
    val total_count: Int,
    val status: String? = null  // "active", "expired", "no_remaining"
)

data class LoginResponse(
    val id: Int,
    val phone: String,
    val name: String,
    val birth: String,  // "2003-06-02" 형식
    val gender: String,
    val membership: Membership
)

data class UserPurchase(
    val productId: List<Int>,
    val productCount: List<Int>,
    val userId: String,
    val userCode: String,
)

data class ProductPurchaseRequest(
    val productIds: List<Int>,
    val productCounts: List<Int>
)

data class CouponPurchaseRequest(
    val couponCode: String,
    val productIds: List<Int>,
    val productCounts: List<Int>
)

data class PhoneLoginRequest(
    val phoneInput: String  // 8자리 또는 전체 번호
)

data class ProductPurchaseResponse(
    val message: String,
    val remainCount: Int,
    val customerName: String,
    val paymentMethod: String,  // "주문번호" 또는 "휴대폰번호"
    val isFirstOrder: Boolean = true
)