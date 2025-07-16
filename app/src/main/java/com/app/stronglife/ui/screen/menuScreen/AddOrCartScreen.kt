package com.app.stronglife.ui.screen.menuScreen


import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.app.stronglife.R
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.black
import com.app.stronglife.viewmodel.MenuScreenViewModel

@Composable
fun AddOrCartScreen(viewModel: MenuScreenViewModel = viewModel ()) {

    val cart by viewModel.cart

    val density = LocalDensity.current
    val textToSp = with(density) {40f.toSp()}
    val btnToDp = with(density) {600f.toDp()}
    val imageToDp = with(density) {358.toDp()}
    val spacetoDp = with(density) {56f.toDp()}
    val imagetoTextDp = with(density) {32f.toDp()}

    Column {
        TopBar(step = 2, listOf("섭취지점 선택", "메뉴선택"))
        Row (
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column (
                modifier = Modifier
                    .width(btnToDp)
                    .height(btnToDp)
                    .background(color = Color.White, shape = RoundedCornerShape(imagetoTextDp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                AsyncImage(
                    model = R.drawable.addorder,
                    contentDescription = "추가 주문하기",
                    modifier = Modifier
                        .width(imageToDp)
                        .height(imageToDp)
                )
                Spacer(modifier = Modifier.height(imagetoTextDp))
                Text(
                    text = "추가 주문하기",
                    style = TextStyle(
                        fontSize = textToSp,
                        fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                        fontWeight = FontWeight.Bold,
                        color = black
                    )
                )
            }

            Spacer(modifier = Modifier.width(spacetoDp))

            Column(
                modifier = Modifier
                    .width(btnToDp)
                    .height(btnToDp)
                    .background(color = Color.White, shape = RoundedCornerShape(imagetoTextDp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                AsyncImage(
                    model = R.drawable.gocart,
                    contentDescription = "장바구니로 이동",
                    modifier = Modifier
                        .width(imageToDp)
                        .height(imageToDp)
                )
                Spacer(modifier = Modifier.height(imagetoTextDp))
                Text(
                    text = "장바구니로 이동",
                    style = TextStyle(
                        fontSize = textToSp,
                        fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                        fontWeight = FontWeight.Bold,
                        color = black
                    )
                )
            }
        }
    }
}

@Preview(
    name = "1920x1080 Landscape",
    showBackground = true,
    device = "spec:width=1920px,height=1080px,dpi=81"
)
@Composable
fun AddOrCartScreenPreview() {
    AddOrCartScreen()
}