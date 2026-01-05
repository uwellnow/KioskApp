package com.app.stronglife.ui.screen.EndScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.stronglife.viewmodel.SurveyViewModel

@Composable
fun QuestionFlow(
    viewModel: SurveyViewModel = viewModel(),
    onFinished: () -> Unit
) {

    val index by viewModel.currentIndex
    val answers by viewModel.answers

    var isAnswered by remember { mutableStateOf(false) }

    val question = QuestionDatas[index]

    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        /*
        when (question) {
            is ChoiceQuestion -> {
                QuestionBox(
                    question = question,
                    questionIndex = index,
                    onAnswered = { viewModel.selectAnswer(question.index, it) }
                )
            }
            is ScoreQuestion -> {
                QuestionBarBox(
                    question = question,
                    questionIndex = index,
                    onAnswered = { score ->
                        viewModel.selectAnswer(question.index, score.toString())
                    }
                )
            }
        }
        * */
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
                if (index < QuestionDatas.lastIndex) {
                    viewModel.next()
                } else {
                    onFinished()
                }
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
    }
}




@Preview(device = "spec:width=1920px,height=1080px,dpi=82")
@Composable
fun QuestionFlowPreview() {
    QuestionFlow(onFinished = {})
}