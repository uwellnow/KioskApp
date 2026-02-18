package com.app.stronglife.ui.screen.EndScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.stronglife.R
import com.app.stronglife.ui.theme.Stronglife
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.lightGray
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.shadowGray
import com.app.stronglife.ui.theme.superLightGray

@Composable
fun QuestionBox(
    question: ChoiceQuestion, 
    questionIndex: Int, 
    existingAnswer: String? = null,
    onAnswered: (String) -> Unit
) {
    val density = LocalDensity.current
    val boxHor = with(density) {20f.toDp()}
    val heightSpaceDp = with(density) {22f.toDp()}
    val widthSpaceDp = with(density) {30f.toDp()}

    // 복수 선택 지원: Set으로 여러 개 선택 관리
    var selectedIndices by remember { mutableStateOf<Set<Int>>(emptySet()) }

    // 기존 답변을 기반으로 초기 선택 상태 복원
    LaunchedEffect(questionIndex, existingAnswer) {
        if (existingAnswer != null && existingAnswer.isNotBlank()) {
            if (question.isMultiple) {
                // 복수 선택: 쉼표로 구분된 문자열을 파싱
                val selectedChoices = existingAnswer.split(",").map { it.trim() }
                selectedIndices = selectedChoices.mapNotNull { choice ->
                    question.choices.indexOf(choice).takeIf { it >= 0 }
                }.toSet()
            } else {
                // 단일 선택
                val index = question.choices.indexOf(existingAnswer)
                selectedIndices = if (index >= 0) setOf(index) else emptySet()
            }
        } else {
            selectedIndices = emptySet()
        }
    }

    // 선택이 변경될 때마다 답변 업데이트
    LaunchedEffect(selectedIndices) {
        if (question.isMultiple) {
            // 복수 선택: 선택된 항목들을 쉼표로 구분하여 전달
            if (selectedIndices.isNotEmpty()) {
                val selectedChoices = selectedIndices.map { question.choices[it] }
                onAnswered(selectedChoices.joinToString(","))
            }
        } else {
            // 단일 선택: 하나만 선택 가능
            if (selectedIndices.isNotEmpty()) {
                val selectedIndex = selectedIndices.first()
                onAnswered(question.choices[selectedIndex])
            }
        }
    }

    Column (
        modifier = Modifier.fillMaxWidth().padding(vertical = boxHor),
        ){

        Column (
            verticalArrangement = Arrangement.spacedBy(heightSpaceDp)
        ){
            question.choices.chunked(2).forEach { rowChoices ->
                Row (
                    horizontalArrangement = Arrangement.spacedBy(widthSpaceDp)
                ){
                    rowChoices.forEachIndexed { rowIndex, choice ->
                        val globalIndex = question.choices.indexOf(choice)
                        val imageResId = question.choiceImages?.getOrNull(globalIndex) ?: R.drawable.radio_btn

                        ChoiceItemWithImage(
                            choice = choice,
                            isSelected = selectedIndices.contains(globalIndex),
                            imageResId = imageResId
                        ) {
                            if (question.isMultiple) {
                                // 복수 선택: 토글 방식, 최대 3개까지 선택 가능
                                selectedIndices = if (selectedIndices.contains(globalIndex)) {
                                    // 이미 선택된 항목이면 제거
                                    selectedIndices - globalIndex
                                } else {
                                    // 최대 3개까지만 선택 가능
                                    if (selectedIndices.size < 3) {
                                        selectedIndices + globalIndex
                                    } else {
                                        selectedIndices // 이미 3개 선택됨, 추가 불가
                                    }
                                }
                            } else {
                                // 단일 선택: 하나만 선택 가능
                                selectedIndices = setOf(globalIndex)
                            }
                        }
                    }
                    // 홀수 개일 경우 빈 공간 추가
                    if (rowChoices.size == 1) {
                        Spacer(modifier = Modifier.width(685.dp + 18.dp))
                    }
                }
            }
        }
    }
}


@Preview(device = "spec:width=1920px,height=1080px,dpi=82", apiLevel = 33, showBackground = true)
@Composable
fun QuestionFlowV1Preview() {
    // ViewModel 없이 UI만 표시하는 간단한 Preview
    val currentIndex = remember { mutableStateOf(9) }
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
