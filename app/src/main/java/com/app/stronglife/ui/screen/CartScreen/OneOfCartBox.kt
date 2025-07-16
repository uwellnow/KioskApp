package com.app.stronglife.ui.screen.CartScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.app.stronglife.data.model.Product
import com.app.stronglife.R
import com.app.stronglife.data.model.CartItem
import java.nio.file.WatchEvent

@Composable
fun OneOfCartBox(cartItem: CartItem, viewModel: CartViewModel ) {
    val product = cartItem.product
    val quantity = cartItem.quantity

    val density = LocalDensity.current

    val titleToSp = with(density) {40f.toSp()}
    val imageToDp = with(density) {200f.toDp()}
    val horPad = with(density) {60f.toDp()}
    val roundDp = with(density) {28f.toDp()}
    val heightDp = with(density) {264f.toDp()}
    val spaceDp = with(density) {33f.toDp()}


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(heightDp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(roundDp)
            )
            .padding(horizontal = horPad),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = product.imageUrl2,
            contentDescription = product.title,
            modifier = Modifier
                .width(imageToDp)
                .height(imageToDp),
        )
        Spacer(modifier = Modifier.width(spaceDp))

        Column (
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = product.title,
                style = TextStyle(
                    fontSize = titleToSp,
                    fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(roundDp))

            CountBtn(
                count = quantity,
                isOne = quantity == 1,
                onIncrease = { viewModel.addProduct(product) },
                onDecrease = { viewModel.decreaseProduct(product) }
            )


        }
        Spacer(modifier = Modifier
            .weight(1f))
        DeleteBtn()
    }
}