package com.app.stronglife.ui.screen.menuScreen



data class Nutrition(
    val name: String,
    val value: String,
    val unit: String,
    val category: String
)

fun parseNutritionInfo(rawInfo: String): List<Nutrition> {
    return rawInfo.split(";")
        .mapNotNull { item ->
            val parts = item.split("|")
            if (parts.size < 4) return@mapNotNull null

            val name = parts[0].trim()
            val value = parts[1].trim()
            val unit = parts[2].trim()
            val category = parts[3].trim()

            Nutrition(name, value, unit, category)

        }
}

fun filterFunctionalNutrients(nutritions: List<Nutrition>): List<Nutrition> {
    return nutritions.filter { it.category == "기능성 영양소" || it.category == "기능성"}
}

fun filterInformationNutrients(nutritions: List<Nutrition>): List<Nutrition> {
    return nutritions.filter { it.category == "정보" }
}

fun filterNormalNutrients(nutritions: List<Nutrition>): List<Nutrition> {
    return nutritions.filter { it.category == "일반 영양소" || it.category == "일반" }
}
