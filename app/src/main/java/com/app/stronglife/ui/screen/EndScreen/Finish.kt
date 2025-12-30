package com.app.stronglife.ui.screen.EndScreen

import CartViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.packInts
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.stronglife.R
import com.app.stronglife.ui.component.ErrorBox
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.descGray

@Composable
fun Finish(
    currentDrinkIndex: Int,
    totalDrinkCount: Int,
    isInProgress: Boolean,
    errorMessage: String? = null,
    onErrorConfirm: (() -> Unit)? = null
) {
    val density = LocalDensity.current
    val barWidDp = with(density) {1140f.toDp()}
    val titleSp = with(density) {70f.toSp()}
    val descSp = with(density) {32f.toSp()}
    val counterSp = with(density) { 36f.toSp() }
    val space1Dp = with(density) {120f.toDp()}
    val space2Dp = with(density) {32f.toDp()}

    if (errorMessage != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            // ErrorBox 시그니처에 맞춤
            ErrorBox(
                errorMsg = "출하 실패",
                desMsg = "제품 출하에 실패했습니다. 환불을 위해 유웰나우 카카오채널로 문의해 주세요"
            ) {
                onErrorConfirm?.invoke()
            }
        }
        return
    }

    Column {

        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){



            Text(
                text = if(isInProgress) stringResource(R.string.pay_done_title) else "소중한 의견 감사드립니다 \uD83D\uDE47\u200D♀\uFE0F",
                fontSize = titleSp,
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                color = black,
            )
            Spacer(modifier = Modifier.height(space2Dp))
            Text(
                text = if(isInProgress) stringResource(R.string.pay_done_desc) else "음료 투출까지 잠시만 기다려주세요 !",
                fontSize = descSp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                color = descGray,
            )
            Spacer(modifier = Modifier.height(space2Dp))

            if (isInProgress) {
                Column(
                    modifier = Modifier.width(barWidDp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (totalDrinkCount > 1) {
                        Text(
                            text = "${totalDrinkCount}잔 중, ${currentDrinkIndex}잔 째 만드는 중입니다",
                            fontSize = counterSp,
                            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                            color = Color(0xFF222222),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(space1Dp))
                    } else {
                        Spacer(Modifier.height(space1Dp))
                    }

                    MakingBar(
                        modifier = Modifier.fillMaxWidth(),
                        stepSeconds = 20,
                        resetKey = currentDrinkIndex
                    )
                }
            }




        }
    }
}

@Preview(showBackground = true, device = "spec:width=1920px,height=1080px,dpi=82")
@Composable
fun FinishPreview() {
    Finish(
        currentDrinkIndex = 1,
        totalDrinkCount = 2,
        isInProgress = true,
        errorMessage = null,
        onErrorConfirm = {}
    )
}
