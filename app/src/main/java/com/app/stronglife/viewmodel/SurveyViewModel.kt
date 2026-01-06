package com.app.stronglife.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stronglife.data.model.SurveyAnswer
import com.app.stronglife.data.model.SurveyRequest
import com.app.stronglife.data.remote.ApiService
import com.app.stronglife.data.remote.KioskLogger
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
        onError: () -> Unit,
        kioskLogger: KioskLogger? = null
    ) {
        viewModelScope.launch {
            try {
                val request = SurveyRequest(
                    answers = answers.value.map { (q,a) ->
                        SurveyAnswer(q,a)
                    }
                )

                println("Survey 제출 시작 - API Key: $apiKey, UserCode: $userCode")
                println("Survey 요청 데이터: ${request.answers}")

                val response = api.submitSurvey(
                    apiKey = apiKey,
                    userCode = userCode,
                    request = request
                )

                if (response.isSuccessful) {
                    println("Survey 제출 성공")
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = "Survey 제출 실패 - HTTP ${response.code()}: $errorBody"
                    println(errorMessage)
                    
                    // 서버로 에러 로그 전송
                    kioskLogger?.logEvent(
                        detail = "SurveySubmitError: HTTP ${response.code()}, UserCode: $userCode, Answers: ${request.answers}, ErrorBody: $errorBody",
                        isError = true
                    )
                    
                    onError()
                }
            } catch (e: Exception) {
                val errorMessage = "Survey 제출 예외 발생: ${e.message}"
                println(errorMessage)
                e.printStackTrace()
                
                // 서버로 에러 로그 전송
                kioskLogger?.logEvent(
                    detail = "SurveySubmitException: ${e.javaClass.simpleName} - ${e.message}, UserCode: $userCode, Answers: ${answers.value}",
                    isError = true
                )
                
                onError()
            }
        }
    }

    fun reset() {
        _currentIndex.value = 0
        _answers.value = emptyMap()
    }

}