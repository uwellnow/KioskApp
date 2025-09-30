package com.app.stronglife.data.model

import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    @SerializedName("user_name") val userName: String,
    @SerializedName("product_id") val productId: Int
)

data class RecipeRequest(
    @SerializedName("recipe_code") val recipeCode: String
)
