package com.app.stronglife.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.app.stronglife.data.model.SurveyAnswer
import com.app.stronglife.data.model.SurveyRequest

class SurveyViewModel: ViewModel() {

    private val _currentIndex = mutableStateOf(0)
    val currentIndex: State<Int> = _currentIndex

    private val _answers = mutableStateOf<Map<Int, String>>(emptyMap())
    val answers: State<Map<Int, String>> = _answers

    fun selectAnswer (question: Int, answer: String) {
        _answers.value = _answers.value + (question to answer)
    }

    fun next() {
        _currentIndex.value++
    }

    fun prev() {
        _currentIndex.value--
    }

    fun buildRequest(surveyId: Int): SurveyRequest {
        return SurveyRequest(
            surveyId = surveyId,
            answers = _answers.value.map {
                SurveyAnswer(it.key, it.value)
            }
        )
    }
}