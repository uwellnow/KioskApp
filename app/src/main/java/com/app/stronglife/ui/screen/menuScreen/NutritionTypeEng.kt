package com.app.stronglife.ui.screen.menuScreen

import com.app.stronglife.R

enum class NutritionTypeEng(
    val displayName: String,
    val icon: Int,
    val nutrients: Map<String, String>
) {
    ENERGY_FOCUS(
        displayName = "Energy&Focus",
        icon = R.drawable.energy,
        nutrients = mapOf(
            "Natural Caffeine" to "Energy · Focus Mode",
            "L-Theanine" to "Caffeine Harmony · Focus",
            "L-Tyrosine" to "Focus Mode · Drive",
            "Vitamin B Complex (B1, B2, B6, B12)" to "Energy · Condition Care"
        )
    ),
    RECOVERY(
        displayName = "Recovery",
        icon = R.drawable.recovery,
        nutrients = mapOf(
            "BCAA" to "Amino Acids · Recovery",
            "L-Leucine" to "Amino Acids · Post-Workout Recovery",
            "L-Isoleucine" to "Amino Acids · Energy Balance",
            "L-Valine" to "Amino Acids · Condition Care",
            "EAA" to "Balanced Amino Acids · Recovery",
            "L-Glutamine" to "Post-Workout Care · Amino Acid Replenishment",
            "Lactose-Free Whey Protein" to "Easy Digestion · Protein Replenishment",
            "Lactoferrin" to "Condition Care",
            "Immunoglobulin" to "Balance · Condition"
        )
    ),
    PUMP_MOTION(
        displayName = "Pump&Motion",
        icon = R.drawable.pump,
        nutrients = mapOf(
            "L-Arginine" to "Pump Mode · Performance",
            "Watermelon Rind Extract Powder" to "Pump Mode · Endurance",
            "L-Citrulline" to "Pump Mode · Endurance",
            "Malic Acid" to "Post-Workout Condition · Endurance",
            "Himalayan Pink Salt" to "Pump Mode · Hydration",
            "Betaine Anhydrous" to "Pump Mode · Endurance"
        )
    ),
    REFRESH(
        displayName = "Refresh",
        icon = R.drawable.refresh,
        nutrients = mapOf(
            "Taurine" to "Condition Care · Muscle Support",
            "Sodium Citrate (DL-Malate)" to "Condition Care · Refresh",
            "Vitamin C" to "Pre/Post-Workout Refresh",
            "Inulin" to "Light Digestion · Absorption",
            "Minerals" to "Vitality / Balance"
        )
    )
}

fun getNutrientMetaEng(name: String): NutrientMeta<NutritionTypeEng>? {
    println("getNutrientMetaEng called with name: '$name'")
    val type = NutritionTypeEng.values().firstOrNull { it.nutrients.containsKey(name) }
    println("Found type: ${type?.displayName}")
    return type?.let {
        NutrientMeta(
            type = it,
            icon = it.icon,
            description = it.nutrients[name] ?: "",
            displayName = it.displayName
        )
    }
}
