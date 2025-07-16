package com.app.stronglife.ui.screen.CartScreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.app.stronglife.data.model.Product

@Composable
fun OneOfCartBox(product: Product) {
    val density = LocalDensity.current

    val titleToSp = with(density) {40f.toSp()}
    val countToSp = with(density) {48f.toDp()}
    val imageToDp = with(density) {200f.toDp()}
    val horPad = with(density) {60f.toDp()}
    val verPad = with(density) {32f.toDp()}
    val roundDp = with(density) {28f.toDp()}

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {

    }
}