package com.app.stronglife.ui.screen.menuScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.stronglife.R
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.midGray

@Composable
fun ProductDetail (image:Int, title:String, nut:String , onClose: () -> Unit, onAddToCart: () -> Unit, onGoCart: () -> Unit) {
    val density = LocalDensity.current
    val widthtoDp = with(density) {1649f.toDp()}
    val heighttoDp = with(density) {776.toDp()}
    val titletoSp = with(density) {40f.toSp()}
    val nuttoSp = with(density) {24f.toSp()}
    val imagetoDp = with(density) {320f.toDp()}
    val roundtoDp = with(density) {20f.toDp()}
    val imagetoTextDp = with(density) {49f.toDp()}
    val spacerToDp = with(density) {230f.toDp()}

    Column(
        modifier = Modifier
            .width(widthtoDp)
            .height(heighttoDp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(roundtoDp)
            ),

    ) {
        Row (
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(70.dp)
        ) {
            AsyncImage(
                model = image,
                contentDescription = title,
                modifier = Modifier
                    .width(imagetoDp)
                    .height(imagetoDp)
            )
            Column (
                modifier = Modifier.padding(start = imagetoTextDp, top = imagetoTextDp*2)
            ){
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = titletoSp,
                        fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                        fontWeight = FontWeight.Bold,
                        color = black
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = nut,
                    style = TextStyle(
                        fontSize = nuttoSp,
                        fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                        fontWeight = FontWeight.Normal,
                        color = midGray
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(spacerToDp))
        MenuScreenBtn(onBackClick = onClose, onCartClick = {onAddToCart()
            onGoCart()})
    }

}

@Preview
@Composable
fun ProductDetailPreview() {
    ProductDetail(R.drawable.id1pro, "삼대오백 프리워크아웃 포도맛", "카페인 300mg의 고함량  L-아르기닌, EAA\n~카페인 300mg의 고함량 L-아르기닌, EAA", onClose = {}, onAddToCart = {}, onGoCart = {})
}