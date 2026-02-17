package com.app.stronglife.ui.screen.EndScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.stronglife.R
import com.app.stronglife.data.remote.KioskLogger
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.viewmodel.SurveyViewModel
import com.app.stronglife.viewmodel.SurveyViewModelFactory
import com.app.stronglife.viewmodel.UserCodeViewModel

@Composable
fun QuestionFlow(
    apiKey: String,
    viewModel: SurveyViewModel = viewModel(factory = SurveyViewModelFactory(RetrofitClient.api)),
    onFinished: () -> Unit,
    onError: () -> Unit,
    kioskLogger: KioskLogger? = null
) {
    val density = LocalDensity.current

    val userCodeViewModel = UserCodeViewModel.getInstance(com.app.stronglife.data.remote.RetrofitClient.api)
    val userCode by userCodeViewModel.userCode
    val userName = userCodeViewModel.loginResponse.value?.name

    val index by viewModel.currentIndex
    val answers by viewModel.answers
    val titleSp = with(density) {28f.toSp()}
    val descSp = with(density) {48f.toSp()}
    var isAnswered by remember { mutableStateOf(false) }

    val question = QuestionDatas[index]

    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ){
        Text(
            text = "${userName}님, 첫 주문이시네요",
            fontSize = titleSp,
            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
            color = mainRed,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${question.title}",
            fontSize = descSp,
            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
            color = black,
        )
        QuestionRenderer(
            question  = question,
            questionIndex = index,
            onAnswered = { answer ->
                viewModel.selectAnswer(question.index, answer)
                isAnswered = true
            }
        )

        PrevNextBox(
            isAnswered = isAnswered,
            onPrev = {
               viewModel.prev()
            },
            onNext = {
                viewModel.next(
                    totalCount = QuestionDatas.size,
                    onFinished = {
                        viewModel.submitSurvey(
                            apiKey = apiKey,
                            userCode = if (userCode.isNotEmpty()) userCode else null,
                            onSuccess = { onFinished() },
                            onError = { onError() },
                            kioskLogger = kioskLogger
                        )
                    }
                )
            }
        )

    }
}



@Composable
fun QuestionRenderer(
    question: QuestionData,
    questionIndex: Int,
    onAnswered: (String) -> Unit
) {
    when (question) {
        is ChoiceQuestion -> {
            QuestionBox(
                question = question,
                questionIndex = questionIndex,
                onAnswered = { answer ->
                    onAnswered(answer)
                }
            )
        }

        is ScoreQuestion -> {
            QuestionBarBox(
                question = question,
                questionIndex = questionIndex,
                onAnswered = {score ->
                    onAnswered(score.toString())
                }
            )
        }

        is CheckQuestion -> {
            QuestionCheckBox(
                question = question,
                questionIndex = questionIndex,
                onAnswered = {score ->
                    onAnswered(score.toString())
                }
            )
        }
    }
}





@Preview(device = "spec:width=1920px,height=1080px,dpi=82", apiLevel = 33, showBackground = true)
@Composable
fun QuestionFlowPreview() {
    // ViewModel 없이 UI만 표시하는 간단한 Preview
    val currentIndex = remember { mutableStateOf(0) }
    val question = QuestionDatas[currentIndex.value]
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "태란님, 첫 주문이시네요",
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
            color = mainRed,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${question.title}",
            fontSize = 30.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
            color = black,
        )
        QuestionRenderer(
            question = question,
            questionIndex = currentIndex.value,
            onAnswered = { }
        )
    }
}
