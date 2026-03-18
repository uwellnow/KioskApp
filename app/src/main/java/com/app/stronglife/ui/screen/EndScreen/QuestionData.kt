package com.app.stronglife.ui.screen.EndScreen

import com.app.stronglife.R

sealed class QuestionData {
    abstract val index: Int
    abstract val title: String
}

data class ChoiceQuestion(
    override val index: Int,
    override val title: String,
    val choices: List<String>,
    val choiceImages: List<Int>? = null,
    val isMultiple: Boolean = false  // 복수 선택 가능 여부
) : QuestionData()

data class CheckQuestion(
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
        title = "직업은 무엇인가요?",
        choices = listOf(
            "학생 (중/고/대학생)",
            "자영업/프리랜서",
            "직장인",
            "기타/무직"
        ),
        choiceImages = listOf(
            R.drawable.survey_student,
            R.drawable.survey_pre,
            R.drawable.survey_employee,
            R.drawable.survey_nojob
        )
    ),
    ChoiceQuestion(
        index = 2,
        title = "건강 목표는 무엇인가요?(최대 3개)",
        choices = listOf(
            "뼈 및 관절 건강",
            "뇌 건강, 집중력 및 인지력",
            "에너지 균형",
            "장 및 소화기 건강",
            "면역 건강",
            "수면의 질",
            "GLP-1 (체중 관리 및 혈당 조절 관련)",
            "모발, 피부 및 손발톱 건강"
        ),
        choiceImages = listOf(
            R.drawable.survey_bone,
            R.drawable.survey_brain,
            R.drawable.survey_energy,
            R.drawable.survey_stomach,
            R.drawable.survey_immune,
            R.drawable.survey_sleep,
            R.drawable.survey_glp,
            R.drawable.survey_beauty
        ),
        isMultiple = true
    ),
    CheckQuestion(
        index = 3,
        title = "기능성 제품(영양제/보충제)은\n얼마나 자주 섭취하시나요?",
        choices = listOf(
            "주 5-7회",
            "주 3-4회",
            "주 1-2회",
            "한달에 1-2회",
            "아예 먹지 않음"
        )
    ),
    CheckQuestion(
        index = 4,
        title = "기능성 제품(영양제/보충제) 섭취 시\n가장 어려운 점은 무엇인가요?",
        choices = listOf(
            "어떤 것을 먹어야 할지 선택하기 어려움",
            "성분을 신뢰하기 어려움",
            "구매/보관/챙기는 과정의 번거로움",
            "가격 대비 효과를 체감하기 어려움",
            "부담되는 가격",
            "섭취 경험 없음"
        )
    ),
    CheckQuestion(
        index = 5,
        title = "오늘 유웰나우를 이용하게 된\n가장 큰 이유는 무엇인가요?",
        choices = listOf(
            "보충제를 들고다니기 귀찮아서",
            "어떤 보충제를 먹어야 할지 몰라서",
            "바로 이용할 수 있어서",
            "특정 건강/운동 고민이 있어서",
            "호기심/신기해서"
        )
    ),
    CheckQuestion(
        index = 6,
        title = "기능성 제품(영양제/보충제),\n선택 기준은 무엇인가요?",
        choices = listOf(
            "저렴한 가격 (가성비)",
            "맛",
            "성분 및 효과",
            "유명한 브랜드의 제품",
            "리뷰 (주변 지인이나 공식몰 후기 등)",
        )
    )
)