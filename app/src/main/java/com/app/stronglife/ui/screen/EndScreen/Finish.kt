package com.app.stronglife.ui.screen.EndScreen

import android.net.Uri
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
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
    
    // 디버깅: isFirstOrder 값 확인
    Log.d("Finish", "isFirstOrder=$isFirstOrder, surveyState=$surveyState")
    val density = LocalDensity.current
    val barWidDp = with(density) {1140f.toDp()}
    val titleSp = with(density) {28f.toSp()}
    val descSp = with(density) {48f.toSp()}
    val counterSp = with(density) { 36f.toSp() }
    val space1Dp = with(density) {120f.toDp()}
    val space2Dp = with(density) {32f.toDp()}
    val space3Dp = with(density) {64f.toDp()}
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

    // 영상 재생: 첫 주문이면 소리만(뒤에서 재생), 기존 고객이면 풀스크린
    val player = remember {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()
        ExoPlayer.Builder(context)
            .setAudioAttributes(audioAttributes, true)
            .build().apply {
                repeatMode = Player.REPEAT_MODE_ALL
                playWhenReady = true
            }
    }
    val playerView = remember {
        PlayerView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            this.player = player
        }
    }
    DisposableEffect(videoUri) {
        player.setMediaItem(MediaItem.fromUri(videoUri))
        player.prepare()
        player.play()
        onDispose {
            player.release()
            // onFinishDispose는 EndScreen의 DisposableEffect에서 호출하므로 여기서는 호출 안 함
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 영상 레이어: 기존 고객이거나 설문 완료 시 보이게, 첫 주문 설문 중이면 투명(소리만)
        val showVideo = !isFirstOrder || surveyState == SurveyState.SUCCESS
        AndroidView(
            factory = { playerView },
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = if (showVideo) 1f else 0f }
        )
        // 첫 주문이고 설문 진행 중일 때만 설문 오버레이 표시
        // isFirstOrder가 false면 절대 설문이 보이지 않음
        val shouldShowSurvey = isFirstOrder && surveyState == SurveyState.IN_PROGRESS
        Log.d("Finish", "shouldShowSurvey=$shouldShowSurvey (isFirstOrder=$isFirstOrder, surveyState=$surveyState)")
        if (shouldShowSurvey) {
            // 첫 주문: 설문 + 이미지 오버레이
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
                Image(
                    painter = painterResource(id = R.drawable.right_qr_section),
                    contentDescription = "프로모션 이벤트 배너",
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
        // isFirstOrder가 false면 여기 아무것도 렌더링 안 함 (영상만 보임)
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