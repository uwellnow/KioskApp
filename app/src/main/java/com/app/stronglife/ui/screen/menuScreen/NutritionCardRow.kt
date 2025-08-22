package com.app.stronglife.ui.screen.menuScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.stronglife.R

@Composable
fun NutritionCardRow(nutrients: List<Nutrition>, lang: String) {
    val density = LocalDensity.current
    val spaceDp = with(density) {14f.toDp()}

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Row(
            horizontalArrangement = Arrangement.spacedBy(spaceDp),
        ) {
            nutrients.forEach { nutrient ->
                NutrientCard(nutrition = nutrient, lang = lang)
            }
        }
    }
}