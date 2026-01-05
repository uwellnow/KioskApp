package com.app.stronglife.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stronglife.data.model.SurveyAnswer
import com.app.stronglife.data.model.SurveyRequest
import com.app.stronglife.data.remote.ApiService
import kotlinx.coroutines.launch

class SurveyViewModel(
    private val api: ApiService
): ViewModel() {

    private val _currentIndex = mutableStateOf(0)
    val currentIndex: State<Int> = _currentIndex

    private val _answers = mutableStateOf<Map<Int, String>>(emptyMap())
    val answers: State<Map<Int, String>> = _answers

    fun selectAnswer (question: Int, answer: String) {
        _answers.value = _answers.value + (question to answer)
    }

    fun next(totalCount: Int, onFinished: () -> Unit) {
        if (currentIndex.value < totalCount - 1) {
            _currentIndex.value++
        } else {
            onFinished()
        }
    }

    fun prev() {
        _currentIndex.value--
    }

    fun getAnswer(question: Int): String? {
        return _answers.value[question]
    }

    fun submitSurvey(
        apiKey: String,
        userCode: String?,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = SurveyRequest(
                    answers = answers.value.map { (q,a) ->
                        SurveyAnswer(q,a)
                    }
                )

                api.submitSurvey(
                    apiKey = apiKey,
                    userCode = userCode,
                    request = request
                )

                onSuccess()
            } catch (e: Exception) {
                onError()
            }
        }
    }

}