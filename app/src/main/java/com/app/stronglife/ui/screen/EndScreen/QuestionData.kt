package com.app.stronglife.ui.screen.EndScreen

sealed class QuestionData {
    abstract val index: Int
    abstract val title: String
}

data class ChoiceQuestion(
    override val index: Int,
    override val title: String,
    val choices: List<String>
) : QuestionData()

data class ScoreQuestion(
    override val index: Int,
    override val title: String,
    val min: Int = 1,
    val max: Int = 5,
    val labels: List<String>
) : QuestionData()


val QuestionDatas = listOf(
    ChoiceQuestion(
        index = 1,
        title = "당신의 직업은 무엇인가요?",
        choices = listOf(
            "학생 (중/고/대학생)",
            "자영업/프리랜서",
            "직장인",
            "기타/무직"
        )
    ),
    ChoiceQuestion(
        index = 2,
        title = "운동을 하는 목표는 무엇인가요?",
        choices = listOf(
            "벌크업 (중량)",
            "다이어트 (감량)",
            "컨디션/체력 개선 (건강 개선)",
            "유지/회복"
        )
    ),
    ScoreQuestion(
        index = 3,
        title = "유웰나우를 주변 분께 추천할 의향이 있으신가요?",
        labels = listOf(
            "추천하지 않을래요",
            "잘 모르겠어요",
            "보통이에요",
            "추천할 것 같아요",
            "꼭 추천할래요"
        )
    )
)