package com.app.stronglife.ui.screen.firstScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.stronglife.R
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.background
import com.app.stronglife.ui.theme.boldGray
import com.app.stronglife.ui.theme.descGray

@Composable
fun FirstScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel()
) {
    val density = LocalDensity.current
    val titleInSp = with(density) {70f.toSp()}
    val paddingInDp = with(density) {80f.toDp()}
    val horpaddingInDp = with(density) {102f.toDp()}
    val contentInSp = with(density) {36f.toSp()}
    val horInDp = with(density) {16f.toDp()}
    val spaceDp = with (density) {28f.toDp()}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        TopBar(step = 1, pageNames = listOf("섭취시점 선택"), navController = navController, cartViewModel = cartViewModel)
        Text(
            text = stringResource(R.string.first_title),
            style = TextStyle(
                fontSize = titleInSp,
                lineHeight = titleInSp * 1.25,
                letterSpacing = (-2).sp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontWeight = FontWeight.Bold,
                color = boldGray
            ),
            modifier = Modifier.padding(start = paddingInDp, top = horpaddingInDp)
        )
        Spacer(modifier = Modifier.height(horInDp))
        Text(
            text = stringResource(R.string.first_desc),
            style = TextStyle(
                fontSize = contentInSp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontWeight = FontWeight.Normal,
                color = descGray
            ),
            modifier = Modifier.padding(start = paddingInDp)
        )
        Spacer(modifier = Modifier.height(horInDp))
        Row(
            modifier = Modifier.padding(start = paddingInDp, top = paddingInDp),
            horizontalArrangement = Arrangement.spacedBy(spaceDp)
        ){
            TimeSelectBtn(stringResource(R.string.first_before_title), stringResource(R.string.first_before_desc), "Pre-\nworkout", navController)
            TimeSelectBtn(stringResource(R.string.first_during_title), stringResource(R.string.first_during_desc), "Intra-\nworkout", navController)
            TimeSelectBtn(stringResource(R.string.first_after_title), stringResource(R.string.first_after_desc), "Post-\nworkout", navController)
        }
    }
}

@Preview(showBackground = true,
    device = "spec:width=1920px,height=1080px,dpi=82")
@Composable
fun FirstPreview() {
    FirstScreen(navController = rememberNavController())
}
