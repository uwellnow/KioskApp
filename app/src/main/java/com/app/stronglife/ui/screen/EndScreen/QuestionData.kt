package com.app.stronglife.ui.screen.EndScreen

data class QuestionData(
    val index: Int,
    val title: String,
    val choices: List<String>
)

val QuestionDatas = listOf(
    QuestionData(
        index = 1,
        title = "당신의 직업은 무엇인가요?",
        choices = listOf(
            "학생 (중/고/대학생)",
            "자영업/프리랜서",
            "직장인",
            "기타/무직"
        )
    ),
    QuestionData(
        index = 2,
        title = "운동을 하는 목표는 무엇인가요?",
        choices = listOf(
            "벌크업 (중량)",
            "다이어트 (감량)",
            "컨디션/체력 개선 (건강 개선)",
            "유지/회복"
        )
    )
)