package com.app.stronglife.ui.screen.PayScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.app.stronglife.R
import com.app.stronglife.ui.theme.background
import com.app.stronglife.ui.theme.boldGray

@Composable
fun KeyPad(onNumberClick: (String) -> Unit,
           onDeleteClick: () -> Unit,
           onClearClick: () -> Unit) {
    val density = LocalDensity.current
    val colDp = with(density) {9f.toDp()}
    val rowdp = with(density) {39f.toDp()}
    val sizeDp = with (density) {96f.toDp()}
    val deleteSp = with(density) {20f.toSp()}
    val removeSp = with(density) {23f.toSp()}

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(colDp)
    ){
        Row (
            horizontalArrangement = Arrangement.spacedBy(rowdp)
        ){
            NumberBtn("1"){ onNumberClick("1") }
            NumberBtn("2"){ onNumberClick("2") }
            NumberBtn("3"){ onNumberClick("2") }
        }


        Row (
            horizontalArrangement = Arrangement.spacedBy(rowdp)
        ){
            NumberBtn("4"){ onNumberClick("4") }
            NumberBtn("5"){ onNumberClick("5") }
            NumberBtn("6"){ onNumberClick("6") }
        }


        Row (
            horizontalArrangement = Arrangement.spacedBy(rowdp)
        ){
            NumberBtn("7"){ onNumberClick("7") }
            NumberBtn("8"){ onNumberClick("8") }
            NumberBtn("9"){ onNumberClick("9") }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(rowdp)
        ) {
            Box(
                modifier = Modifier.size(sizeDp, sizeDp).background(background, shape = CircleShape)
                    .clickable{onClearClick()},
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "전체\n삭제",
                    style = TextStyle(
                        fontSize = deleteSp,
                        lineHeight = deleteSp * 1.1,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = boldGray
                    ),
                )
            }

            NumberBtn("0"){ onNumberClick("0") }

            Box(
                modifier = Modifier.size(sizeDp, sizeDp).background(background, shape = CircleShape)
                    .clickable{onDeleteClick()},
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "지\n움",
                    style = TextStyle(
                        fontSize = removeSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = boldGray
                    ),
                )
            }
        }

    }
}

