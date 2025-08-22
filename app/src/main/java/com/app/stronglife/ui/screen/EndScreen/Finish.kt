package com.app.stronglife.ui.screen.EndScreen

import CartViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.packInts
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.stronglife.R
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.descGray

@Composable
fun Finish() {
    val density = LocalDensity.current
    val imageDp = with(density) {178f.toDp()}
    val titleSp = with(density) {70f.toSp()}
    val descSp = with(density) {32f.toSp()}

    val space1Dp = with(density) {60f.toDp()}
    val space2Dp = with(density) {32f.toDp()}

    Column {

        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(R.drawable.pay_done),
                contentDescription = "결제 완료",
                modifier = Modifier.size(imageDp)
            )
            Spacer(modifier = Modifier.height(space1Dp))

            Text(
                text = stringResource(R.string.pay_done_title),
                fontSize = titleSp,
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                color = black,
            )
            Spacer(modifier = Modifier.height(space2Dp))
            Text(
                text = stringResource(R.string.pay_done_desc),
                fontSize = descSp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                color = descGray,
            )
        }
    }
}


