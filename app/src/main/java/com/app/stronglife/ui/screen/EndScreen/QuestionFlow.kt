package com.app.stronglife.ui.screen.EndScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun QuestionFlow(
    onFinished: () -> Unit
) {
    var currentIndex by remember { mutableStateOf(0) }
    var isAnswered by remember { mutableStateOf(false) }

    val question = QuestionDatas[currentIndex]

    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        QuestionBox(
            question = question,
            onAnswered = {isAnswered = true}
        )

        PrevNextBox(
            isAnswered = isAnswered,
            onPrev = {
                if (currentIndex > 0) {
                    currentIndex--
                    isAnswered = false
                }
            },
            onNext = {
                if (currentIndex < QuestionDatas.lastIndex) {
                    currentIndex++
                    isAnswered = false
                } else {
                    onFinished()
                }
            }
        )

    }
}

@Preview
@Composable
fun QuestionFlowPreview() {
    QuestionFlow(onFinished = {})
}