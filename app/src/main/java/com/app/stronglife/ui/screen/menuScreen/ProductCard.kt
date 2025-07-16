package com.app.stronglife.ui.screen.menuScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.core.provider.FontsContractCompat.FontRequestCallback.FontRequestFailReason
import coil.compose.AsyncImage
import com.app.stronglife.R
import com.app.stronglife.data.model.Product
import com.app.stronglife.mock.sampleProducts
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.lightGray
import com.app.stronglife.ui.theme.midGray

@Composable
fun ProductCard(products: List<Product>, onProductClick: (Product) -> Unit) {
    val density = LocalDensity.current
    val imagePadding = with(density) { 100f.toDp() }
    val horPadding = with(density) { 20.toDp() }
    val widthtoDp = with(density) { 513f.toDp() }
    val heighttoDp = with(density) { 705f.toDp() }
    val imageSize = with(density) { 400f.toDp() }
    val desfont = with(density) { 20f.toSp() }
    val titlefont = with(density) { 32f.toSp() }
    val textSpace = with(density) { 30f.toDp() }

    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .horizontalScroll(scrollState)
            .padding(start = 70.dp)
    ) {
        products.forEach { product ->
            Column(
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                TimeCategory(product.time)
                Box(
                    modifier = Modifier
                        .width(widthtoDp)
                        .height(heighttoDp)
                        .shadow(2.dp, shape = RoundedCornerShape(topEnd = horPadding,
                            bottomStart = horPadding,
                            bottomEnd = horPadding),
                            ambientColor = lightGray, spotColor = lightGray)

                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(
                                topEnd = horPadding,
                                bottomStart = horPadding,
                                bottomEnd = horPadding
                            )
                        )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = horPadding)
                            .clickable{ onProductClick(product)}
                    ) {
                        AsyncImage(
                            model = product.imageUrl1,
                            contentDescription = product.title,
                            modifier = Modifier
                                .padding(start = textSpace)
                                .width(imagePadding)
                                .height(imagePadding)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = product.imageUrl2,
                                contentDescription = product.title,
                                modifier = Modifier
                                    .width(imageSize)
                                    .height(imageSize)
                            )
                            Spacer(modifier = Modifier.height(textSpace))
                            Text(
                                text = product.description,
                                style = TextStyle(
                                    fontSize = desfont,
                                    textAlign = TextAlign.Center,
                                    lineHeight = desfont * 1.2,
                                    fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                                    fontWeight = FontWeight.Normal,
                                    color = midGray
                                )
                            )
                            Spacer(modifier = Modifier.height(textSpace))
                            Text(
                                text = product.title,
                                style = TextStyle(
                                    fontSize = titlefont,
                                    fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                                    fontWeight = FontWeight.Bold,
                                    color = black
                                )
                            )
                        }

                    }


                }


            }
        }
    }
}



