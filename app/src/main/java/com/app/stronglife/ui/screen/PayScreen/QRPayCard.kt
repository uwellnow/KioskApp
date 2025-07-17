package com.app.stronglife.ui.screen.PayScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.stronglife.R
import com.app.stronglife.mock.sampleMembers
import com.app.stronglife.ui.theme.background
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.midGray
import com.app.stronglife.ui.theme.qrScanGray

@Composable
fun QRPayCard(inputId: Int, showInfo: Boolean) {

    val density = LocalDensity.current
    val titleSp = with(density) {36f.toSp()}
    val t2iSpaceDp = with(density) {16f.toDp()}  // box row padding도 같이
    val i2iSpaceDp = with(density) {52f.toDp()}
    val imageDp = with(density) {304f.toDp()}

    val boxwidDp = with(density) {666f.toDp()}
    val boxheightDp = with(density) {178f.toDp()}
    val boxRoundDp = with(density) {24f.toDp()}

    val boxHeiPadDp = with(density) {35f.toDp()}
    val boxWidPadDp = with(density) {51f.toDp()}

    val textRegSp = with(density) {28f.toSp()}
    val textBolSp = with(density) {32f.toSp()}

    // 입력받은 id를 가지고 db에서 인덱스 찾기
    val index = sampleMembers.indexOfFirst { it.id == inputId }
    if (index == -1) {
        println("등록된 회원 정보가 없습니다.")
        return

    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "QR 코드를 스캔해주세요",
            style = TextStyle(
                fontSize = titleSp,
                fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                fontWeight = FontWeight.Bold,
                color = black
            )
        )
        Spacer(modifier = Modifier.height(t2iSpaceDp))
        AsyncImage(
            model = R.drawable.qr,
            contentDescription = "qr코드 스캔",
            modifier = Modifier.width(imageDp).height(imageDp)
        )
        Spacer(modifier = Modifier.height(i2iSpaceDp))

        Column(
            modifier = Modifier
                .width(boxwidDp)
                .height(boxheightDp)
                .background(color = background, RoundedCornerShape(boxRoundDp))
                .padding(horizontal = boxWidPadDp, vertical = boxHeiPadDp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "회원 정보",
                    modifier = Modifier
                        .weight(1f),
                    style = TextStyle(
                        fontSize = textRegSp,
                        fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                        fontWeight = FontWeight.Normal,
                        color = midGray
                    )
                )

                if (showInfo) {
                    Text(
                        text = sampleMembers[index].name + " 고객님"+"("+inputId+")",
                        style = TextStyle(
                            fontSize = textBolSp,
                            fontFamily = FontFamily(Font(R.font.sfpro_semibold)),
                            fontWeight = FontWeight.SemiBold,
                            color = black
                        )
                    )
                }
            }

            Divider(modifier = Modifier.fillMaxWidth().padding(vertical = t2iSpaceDp),color = qrScanGray, thickness = 1.dp)

            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween

            ){
                Text(
                    text = "남은 음료 잔 수",
                    style = TextStyle(
                        fontSize = textRegSp,
                        fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                        fontWeight = FontWeight.Normal,
                        color = midGray
                    )
                )

                if (showInfo) {
                    Text(
                        text = sampleMembers[index].remain.toString() + "잔",
                        style = TextStyle(
                            fontSize = textBolSp,
                            fontFamily = FontFamily(Font(R.font.sfpro_semibold)),
                            fontWeight = FontWeight.SemiBold,
                            color = mainRed
                        )
                    )
                }

            }

        }

    }
}
