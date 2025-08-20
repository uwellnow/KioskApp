package com.app.stronglife.ui.screen.menuScreen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.stronglife.R
import com.app.stronglife.data.model.Product
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.lightGray

@Composable
fun ProductCard(products: List<Product>, onProductClick: (Product) -> Unit) {
    val density = LocalDensity.current
    val imagePadding = with(density) { 100f.toDp() }
    val horPadding = with(density) { 45.toDp() }
    val widthtoDp = with(density) { 513f.toDp() }
    val heighttoDp = with(density) { 787f.toDp() }
    val imageSize = with(density) { 440f.toDp() }
    val desfont = with(density) { 20f.toSp() }
    val titlefont = with(density) { 30f.toSp() }
    val roundDp = with(density) { 12f.toDp() }
    val horpadDp = with(density) {80f.toDp()}
    val textdp = with (density) {65f.toDp()}
    val spaceDp = with (density) {18f.toDp()}

    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .horizontalScroll(scrollState)
            .padding(horizontal = horpadDp)
    ) {
        products.forEach { product ->
            Column(
                modifier = Modifier.padding(horizontal = spaceDp)
            ) {
                Box(
                    modifier = Modifier
                        .width(widthtoDp)
                        .height(heighttoDp)
                        .shadow(
                            2.dp,
                            shape = RoundedCornerShape(
                                roundDp
                            ),
                            ambientColor = lightGray,
                            spotColor = lightGray
                        )
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(roundDp)
                        )
                        .padding(vertical = spaceDp)
                ) {

                    TimeCategory(
                        product.timing,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = spaceDp,end  = 40.dp)
                    )


                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onProductClick(product) }
                    ) {
                        AsyncImage(
                            model = product.companyImagePath,
                            contentDescription = product.name,
                            modifier = Modifier
                                .padding(start = horPadding)
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
                                model = product.productImagePath,
                                contentDescription = product.name,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(imageSize)
                            )

                        }

                        Spacer(modifier = Modifier.height(horPadding))

                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = textdp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(
                                text = product.description.replace("\\n", "\n"),
                                style = TextStyle(
                                    fontSize = desfont,
                                    textAlign = TextAlign.Center,
                                    lineHeight = desfont * 1.3,
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    fontWeight = FontWeight.Normal,
                                    color = lightGray
                                )
                            )
                            Text(
                                text = product.name.replace("\\n", "\n"),
                                modifier = Modifier
                                    .padding(top = spaceDp),
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontSize = titlefont,
                                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
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




