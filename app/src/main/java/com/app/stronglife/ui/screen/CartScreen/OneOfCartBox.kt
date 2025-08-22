package com.app.stronglife.ui.screen.CartScreen

import CartViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.stronglife.R
import com.app.stronglife.data.model.CartItem
import com.app.stronglife.ui.theme.black

@Composable
fun OneOfCartBox(cartItem: CartItem, viewModel: CartViewModel, languageManager: com.app.stronglife.util.LanguageManager) {
    val product = cartItem.product
    val quantity = cartItem.quantity
    val langTag by languageManager.languageTag.collectAsState()

    val density = LocalDensity.current
    val titleToSp = with(density) { 40f.toSp() }
    val imageToDp = with(density) { 200f.toDp() }
    val spaceDp = with(density) { 32f.toDp() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spaceDp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = product.productImagePath,
            contentDescription = product.name,
            modifier = Modifier
                .width(imageToDp)
                .height(imageToDp)
        )

        Spacer(modifier = Modifier.width(spaceDp))

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = (if (langTag == "ko") product.name else (if (product.nameEng.isNotBlank()) product.nameEng else product.name)).replace("\\n", " ").replace("\n", " "),
                style = TextStyle(
                    fontSize = titleToSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontWeight = FontWeight.Bold,
                    color = black
                )
            )

            Spacer(modifier = Modifier.height(spaceDp))

            CountBtn(
                count = quantity,
                isOne = quantity == 1,
                onIncrease = { viewModel.addProduct(product) },
                onDecrease = { viewModel.decreaseProduct(product) }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        DeleteBtn(
            onDelete = {
                viewModel.removeProduct(product)
            }
        )
    }
}
