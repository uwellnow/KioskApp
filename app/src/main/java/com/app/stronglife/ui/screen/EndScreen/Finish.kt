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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.app.stronglife.data.remote.KioskLogger
import com.app.stronglife.ui.theme.mainRed

@Composable
fun Finish(
    apiKey: String,
    currentDrinkIndex: Int,
    totalDrinkCount: Int,
    isInProgress: Boolean,
    surveyState: SurveyState,
    onSurveyFinished: () -> Unit,
    onSurveyError: () -> Unit,
    errorMessage: String? = null,
    onErrorConfirm: (() -> Unit)? = null,
    kioskLogger: KioskLogger? = null
) {
    val density = LocalDensity.current
    val barWidDp = with(density) {1140f.toDp()}
    val titleSp = with(density) {28f.toSp()}
    val descSp = with(density) {48f.toSp()}
    val counterSp = with(density) { 36f.toSp() }
    val space1Dp = with(density) {120f.toDp()}
    val space2Dp = with(density) {32f.toDp()}
    val space3Dp = with(density) {64f.toDp()}
    val horPadding = with(density) {80f.toDp()}
    val verPadding = with(density) {175f.toDp()}

    if (errorMessage != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            ErrorBox(
                errorMsg = "출하 실패",
                desMsg = "제품 출하에 실패했습니다. 환불을 위해 유웰나우 카카오채널로 문의해 주세요"
            ) {
                onErrorConfirm?.invoke()
            }
        }
        return
    }

    if (surveyState == SurveyState.ERROR) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            ErrorBox(
                errorMsg = "설문 전송 실패",
                desMsg = "설문 전송에 실패했습니다."
            ) { onSurveyError() }
        }
        return
    }

    Column (
        modifier = Modifier.fillMaxSize().padding(horizontal = horPadding, verPadding),
        horizontalAlignment = Alignment.Start,
    ){
        if (isInProgress) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                    if (totalDrinkCount > 1) {
//                    } else {
//                    }
                when (surveyState) {
                    SurveyState.IN_PROGRESS -> {
                        QuestionFlow(
                            apiKey = apiKey,
                            onFinished = { onSurveyFinished() },
                            onError = {onSurveyError() },
                            kioskLogger = kioskLogger
                        )
                    }

                    SurveyState.SUCCESS -> {
                    }

                    SurveyState.ERROR -> {
                    }

                }
            }
        }




    }
}

@Preview(showBackground = true, device = "spec:width=1920px,height=1080px,dpi=82", apiLevel = 33)
@Composable
fun FinishPreview() {
    Finish(
        apiKey = "20250000",
        currentDrinkIndex = 1,
        totalDrinkCount = 2,
        isInProgress = true,
        surveyState = SurveyState.IN_PROGRESS,
        onSurveyFinished = {},
        onSurveyError = {},
    )
}
