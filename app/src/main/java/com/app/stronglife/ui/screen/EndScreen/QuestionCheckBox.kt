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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.app.stronglife.ui.theme.Stronglife
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.shadowGray

@Composable
fun QuestionCheckBox(
    question: CheckQuestion, 
    questionIndex: Int, 
    existingAnswer: String? = null,
    onAnswered: (String) -> Unit
) {
    val density = LocalDensity.current

    val boxHor = with(density) {60f.toDp()}
    val heightSpaceDp = with(density) {22f.toDp()}

    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    // 기존 답변을 기반으로 초기 선택 상태 복원
    LaunchedEffect(questionIndex, existingAnswer) {
        if (existingAnswer != null && existingAnswer.isNotBlank()) {
            val index = question.choices.indexOf(existingAnswer)
            selectedIndex = if (index >= 0) index else null
        } else {
            selectedIndex = null
        }
    }

    Column (
        modifier = Modifier.fillMaxWidth().padding(vertical = boxHor),
    ){

        Column (
            verticalArrangement = Arrangement.spacedBy(heightSpaceDp)
        ){
            // 선택지를 2개씩 행으로 나누어 표시 (동적 처리)
            question.choices.chunked(2).forEach { rowChoices ->
                Row (
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ){
                    rowChoices.forEachIndexed { rowIndex, choice ->
                        val globalIndex = question.choices.indexOf(choice)

                        CheckItem(
                            choice = choice,
                            isSelected = selectedIndex == globalIndex
                        ) {
                            selectedIndex = globalIndex
                            onAnswered(choice)
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
