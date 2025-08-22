package com.app.stronglife.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    val name: String,
    @SerializedName("name_eng") val nameEng: String,
    val timing: String,
    val description: String,
    @SerializedName("description_eng") val descriptionEng: String,
    @SerializedName("nutrition_info") val nutritionInfo: String,
    @SerializedName("nutrition_info_eng") val nutritionInfoEng: String,
    @SerializedName("company_image_path") val companyImagePath: String,
    @SerializedName("product_image_path") val productImagePath: String,
    @SerializedName("recipe_slots") val recipeSlots: List<List<Int>> = emptyList()
)