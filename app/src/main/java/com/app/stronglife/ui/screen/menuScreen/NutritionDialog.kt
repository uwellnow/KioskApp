package com.app.stronglife.ui.screen.menuScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.stronglife.R
import com.app.stronglife.ui.theme.background
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.lightGray
import com.app.stronglife.ui.theme.superLightGray


@Composable
fun NutritionDialog(
    nutritions: List<Nutrition>,
    onDismiss: () -> Unit
) {
    val info = filterInformationNutrients(nutritions)
    val normal = filterNormalNutrients(nutritions)

    val density = LocalDensity.current
    val titleSp = with(density) {28f.toSp()}
    val widDp = with(density) {387f.toDp()}
    val boxHeiDp = with(density) {51f.toDp()}
    val descSp = with(density) {20f.toSp()}

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { },
        title = { 
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "영양정보",
                    style = TextStyle(
                        fontSize = titleSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                        color = black
                    ),
                    modifier = Modifier.padding(top = 80.dp, start = 40.dp)
                )
                
                // 닫기 버튼을 오른쪽 상단에 위치
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 8.dp, end = 8.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(100.dp, 70.dp)
                    ) {
                        Text(
                            "닫기",
                            style = TextStyle(
                                fontSize = 30.sp,
                                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                                color = black
                            )
                        )
                    }
                }
            }
        },

        containerColor = Color.White,


        text = {

            val textStyle = TextStyle(
                fontSize = descSp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                color = black
            )

            Column (
                modifier = Modifier.width(widDp)
                    .padding( horizontal = 40.dp)

            ){

                Spacer(modifier =Modifier.height(16.dp))
                Row (
                    modifier = Modifier.fillMaxWidth().height(boxHeiDp)
                        .background(superLightGray, RoundedCornerShape(4.dp))
                        .padding(horizontal = 19.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    info.forEach {
                        val label = if (it.name == "열량") "" else it.name
                        Text("$label ${it.value}${it.unit}",
                            style = textStyle)
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(text = "* 괄호 안 수치는 1일 영양성분 기준치에 대한 비율",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                        color = black
                    ),
                    textAlign = TextAlign.End)

                Spacer(Modifier.height(32.dp))

                Column (
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    normal.forEach {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${it.name}", style = textStyle)

                            Text("${it.value}${it.unit}", style = textStyle)
                        }

                    }
                }


            }
        }
    )
}
