package com.app.stronglife.ui.screen.EndScreen

import android.net.Uri
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.packInts
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.app.stronglife.R
import com.app.stronglife.ui.component.ErrorBox
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.descGray
import com.app.stronglife.data.remote.KioskLogger
import com.app.stronglife.ui.theme.mainRed
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

private val VIDEO_RAW_IDS = listOf(R.raw.video1, R.raw.video2, R.raw.video3, R.raw.video4)

@Composable
fun Finish(
    apiKey: String,
    currentDrinkIndex: Int,
    totalDrinkCount: Int,
    isInProgress: Boolean,
    surveyState: SurveyState,
    isFirstOrder: Boolean,
    onSurveyFinished: () -> Unit,
    onSurveyError: () -> Unit,
    errorMessage: String? = null,
    onErrorConfirm: (() -> Unit)? = null,
    onFinishDispose: (() -> Unit)? = null,
    kioskLogger: KioskLogger? = null
) {
    val context = LocalContext.current
    val randomRawId = remember { VIDEO_RAW_IDS.random() }
    val videoUri = remember(randomRawId) {
        Uri.parse("android.resource://${context.packageName}/$randomRawId")
    }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.pay_finish))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    
    // 디버깅: isFirstOrder 값 확인
    Log.d("Finish", "isFirstOrder=$isFirstOrder, surveyState=$surveyState")
    val density = LocalDensity.current
    val barWidDp = with(density) {1140f.toDp()}
    val counterSp = with(density) { 36f.toSp() }
    val space1Dp = with(density) {60f.toDp()}
    val space2Dp = with(density) {32f.toDp()}
    val space3Dp = with(density) {20f.toDp()}
    val titleSp = with(density) {70f.toSp()}
    val descSp = with(density) {32f.toSp()}
    val imageDp = with(density) {80f.toDp()}
    val lottieDp = with(density) {250f.toDp()}
    val horPadding = with(density) {80f.toDp()}
    val verPadding = with(density) {120f.toDp()}

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

    Box(modifier = Modifier.fillMaxSize()) {
        val shouldShowSurvey = isFirstOrder && surveyState == SurveyState.IN_PROGRESS
        val shouldShowSurveyFinish = isFirstOrder && surveyState == SurveyState.SUCCESS
        Log.d("Finish", "shouldShowSurvey=$shouldShowSurvey (isFirstOrder=$isFirstOrder, surveyState=$surveyState)")
        if (shouldShowSurvey) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = horPadding, vertical = verPadding),
                    horizontalAlignment = Alignment.Start,
                ) {
                    QuestionFlow(
                        apiKey = apiKey,
                        onFinished = { onSurveyFinished() },
                        onError = { onSurveyError() },
                        kioskLogger = kioskLogger
                    )
                }
            }
        } else if (shouldShowSurveyFinish) {
            Column (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Row (
                    horizontalArrangement = Arrangement.spacedBy(space3Dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "소중한 의견 감사드립니다 ",
                        style = TextStyle(
                            fontSize = titleSp,
                            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                            color = Color(0xFF484848)
                        )
                    )
                    Image(
                        painter = painterResource(R.drawable.thankyou),
                        contentDescription = "감사드립니다",
                        modifier = Modifier.size(imageDp)
                    )
                }


                Spacer(modifier = Modifier.height(space2Dp))

                Text(
                    text = "보충제 음료 제조가 시작되었습니다. 잠시만 기다려주세요.",
                    style = TextStyle(
                        fontSize = descSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = Color(0xFFAFAFAF)
                    )
                )
            }

        } else {
            Column (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(lottieDp)
                )
                Spacer(modifier = Modifier.height(space1Dp))
                Text(
                    text = "결제가 완료되었어요 !",
                    style = TextStyle(
                        fontSize = titleSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                        color = Color(0xFF484848)
                    )
                )

                Spacer(modifier = Modifier.height(space2Dp))

                Text(
                    text = "보충제 음료 제조가 시작되었습니다. 잠시만 기다려주세요.",
                    style = TextStyle(
                        fontSize = descSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = Color(0xFFAFAFAF)
                    )
                )
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
        isFirstOrder = true,
        onSurveyFinished = {},
        onSurveyError = {},
    )
}