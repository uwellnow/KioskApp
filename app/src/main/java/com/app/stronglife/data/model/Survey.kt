package com.app.stronglife.data.model

data class SurveyAnswer (
    val question: Int,
    val answer: String
)

data class SurveyRequest(
    val surveyId: Int,
    val answers: List<SurveyAnswer>
)