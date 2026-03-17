package com.app.stronglife.ui.screen.PayScreen

import CartViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import com.app.stronglife.R
import com.app.stronglife.data.model.LoginResponse
import com.app.stronglife.ui.theme.background
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.desc2Gray
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.midGray
import com.app.stronglife.ui.theme.qrScanGray

@Composable
fun UserBox(
    showInfo: Boolean, 
    loginResponse: LoginResponse?,
    cartViewModel: CartViewModel
) {

    val density = LocalDensity.current

    val t2iSpaceDp = with(density) {20f.toDp()}
    val boxwidDp = with(density) {763f.toDp()}
    val boxheightDp = with(density) {266f.toDp()}
    val boxRoundDp = with(density) {24f.toDp()}

    val boxHeiPadDp = with(density) {35f.toDp()}
    val boxWidPadDp = with(density) {51f.toDp()}

    val textRegSp = with(density) {28f.toSp()}
    val textBolSp = with(density) {32f.toSp()}


    // 장바구니 총 수량 계산
    val cartTotalQuantity = cartViewModel.cartItems.value.sumOf { it.quantity }

    // 멤버십/무제한 관련 계산
    val membership = loginResponse?.membership
    val isUnlimited = membership?.remain_count == null && (membership?.total_count == 0)

    // 남은 일수 계산 (무제한 전용)
    val daysLeft: Long? = if (isUnlimited && membership.expired_at != null) {
        try {
            val expiredDate = OffsetDateTime.parse(membership.expired_at).toLocalDate()
            val today = LocalDate.now()
            ChronoUnit.DAYS.between(today, expiredDate)
        } catch (_: Exception) {
            null
        }
    } else {
        null
    }

    val unlimitedLabel: String? = if (isUnlimited) {
        if (daysLeft != null && daysLeft > 0) {
            "무제한(${daysLeft}일 남음)"
        } else {
            "무제한"
        }
    } else {
        null
    }

    // 차감 후 수량 계산 (무제한이면 숫자 차감 대신 그대로 무제한 표기)
    val remainingAfterDeduction = if (!isUnlimited && showInfo && loginResponse != null) {
        (membership?.remain_count ?: 0) - cartTotalQuantity
    } else 0

    Column(
        modifier = Modifier
            .width(boxwidDp)
            .height(boxheightDp)
            .background(color = Color.White, RoundedCornerShape(boxRoundDp))
            .padding(horizontal = boxWidPadDp, vertical = boxHeiPadDp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 첫 번째 행: 회원 정보
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = stringResource(R.string.info_box_user),
                style = TextStyle(
                    fontSize = textRegSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontWeight = FontWeight.Normal,
                    color = desc2Gray
                )
            )

            if (showInfo && loginResponse != null) {
                Text(
                    text = "${loginResponse.name} 고객님 (${loginResponse.phone.takeLast(4)})",
                    style = TextStyle(
                        fontSize = textBolSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                        fontWeight = FontWeight.SemiBold,
                        color = black
                    )
                )
            }
        }

        Divider(modifier = Modifier.fillMaxWidth().padding(vertical = t2iSpaceDp), color = qrScanGray, thickness = 1.dp)

        // 두 번째 행: 현재 보유 수량
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = stringResource(R.string.info_box_total),
                style = TextStyle(
                    fontSize = textRegSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontWeight = FontWeight.Normal,
                    color = desc2Gray
                )
            )

            if (showInfo && loginResponse != null) {
                val text = unlimitedLabel ?: "${loginResponse.membership.remain_count}잔"
                Text(
                    text = text,
                    style = TextStyle(
                        fontSize = textBolSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                        fontWeight = FontWeight.SemiBold,
                        color = mainRed
                    )
                )
            }
        }

        Divider(modifier = Modifier.fillMaxWidth().padding(vertical = t2iSpaceDp), color = qrScanGray, thickness = 1.dp)

        // 세 번째 행: 차감 후 수량
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = stringResource(R.string.info_box_remain),
                style = TextStyle(
                    fontSize = textRegSp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontWeight = FontWeight.Normal,
                    color = desc2Gray
                )
            )

            if (showInfo && loginResponse != null) {
                val text = unlimitedLabel ?: "${remainingAfterDeduction}잔"
                Text(
                    text = text,
                    style = TextStyle(
                        fontSize = textBolSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                        fontWeight = FontWeight.SemiBold,
                        color = mainRed
                    )
                )
            }
        }
    }
}