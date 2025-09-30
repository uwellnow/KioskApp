package com.app.stronglife.ui.screen.HelloScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.R
import com.app.stronglife.ui.theme.mainRed
import com.app.stronglife.ui.theme.shadowGray
import kotlinx.coroutines.selects.select


@Composable
fun StartBtn(navController: NavController) {

    val density = LocalDensity.current
    val widDp = with(density) { 824f.toDp() }
    val heiDp = with(density) { 416f.toDp() }
    val imgWidDp = with(density) { 199f.toDp() }
    val imgHeiDp = with(density) { 217f.toDp() }
    val img1WidDp = with(density) { 149f.toDp() }
    val img2HeiDp = with(density) { 266f.toDp() }
    val horDp = with(density) { 44f.toDp() }
    val verDp = with(density) { 99f.toDp() }
    val roundDp = with(density) { 32f.toDp() }

    val blurRadiusPx = with(density) { 24.dp.toPx() }
    val titleSp = with(density) { 52f.toSp() }
    val desSp = with(density) { 32f.toSp() }

    val spaceDp = with(density) { 44f.toDp() }
    val space2Dp = with(density) { 52f.toDp() }

    val selected by remember { mutableStateOf<String?>(null) }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row (
            modifier = Modifier.size(widDp, heiDp)
                .drawBehind {
                    drawIntoCanvas { canvas ->
                        val paint = Paint().asFrameworkPaint().apply {
                            color = shadowGray.toArgb()
                            maskFilter = android.graphics.BlurMaskFilter(
                                blurRadiusPx,
                                android.graphics.BlurMaskFilter.Blur.NORMAL
                            )
                        }
                        canvas.nativeCanvas.drawRoundRect(
                            0f,
                            0f,
                            size.width,
                            size.height,
                            blurRadiusPx,
                            blurRadiusPx,
                            paint
                        )
                    }
                }
                .clickable{ selected == "recipe"
                navController.navigate("recipe")}
                .background(color = if (selected == "recipe") mainRed else Color.White, shape = RoundedCornerShape(roundDp))
                .padding(horizontal = horDp, vertical = verDp),
            horizontalArrangement = Arrangement.spacedBy(spaceDp)
        ){
            Image(
                painter = if (selected == "recipe") painterResource(id = R.drawable.select_recipe_red) else painterResource(id = R.drawable.select_recipe),
                contentDescription = "추천 레시피로 시작하기 아이콘",
                modifier = Modifier.size(imgWidDp, imgHeiDp)
            )

            Column (
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(space2Dp)
            ){
                Text(
                    text = "추천 레시피로 시작하기",
                    style = TextStyle(
                        fontSize = titleSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        fontWeight = FontWeight.Bold,
                        color = if (selected == "recipe") Color.White else Color(0xFF111827)
                    )
                )
                Text(
                    text = "운동 목표와 컨디션까지 고려한\n나만을 위한 레시피로",
                    style = TextStyle(
                        fontSize = desSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontWeight = FontWeight.Medium,
                        color = if (selected == "recipe") Color.White else Color(0xFF949494)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.width(spaceDp))
        Row (
            modifier = Modifier.size(widDp, heiDp)
                .drawBehind {
                    drawIntoCanvas { canvas ->
                        val paint = Paint().asFrameworkPaint().apply {
                            color = shadowGray.toArgb()
                            maskFilter = android.graphics.BlurMaskFilter(
                                blurRadiusPx,
                                android.graphics.BlurMaskFilter.Blur.NORMAL
                            )
                        }
                        canvas.nativeCanvas.drawRoundRect(
                            0f,
                            0f,
                            size.width,
                            size.height,
                            blurRadiusPx,
                            blurRadiusPx,
                            paint
                        )
                    }
                }
                .clickable {
                    selected == "mix"
                    navController.navigate("first")}
                .background(color = if (selected == "mix") mainRed else Color.White, shape = RoundedCornerShape(roundDp))
                .padding(horizontal = horDp, vertical = verDp),
            horizontalArrangement = Arrangement.spacedBy(spaceDp)
        ){
            Image(
                painter = painterResource(id = R.drawable.select_mix),
                contentDescription = "내가 직접 조합하기 아이콘",
                modifier = Modifier.size(img1WidDp, img2HeiDp)
            )

            Column (
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(space2Dp)
            ){
                Text(
                    text = "내가 직접 조합하기",
                    style = TextStyle(
                        fontSize = titleSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        fontWeight = FontWeight.Bold,
                        color = if (selected == "mix") Color.White else Color(0xFF111827)
                    )
                )
                Text(
                    text = "원하는 성분과 함량을 직접 선택해\n나만의 조합을 완성하세요",
                    style = TextStyle(
                        fontSize = desSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontWeight = FontWeight.Medium,
                        color = if (selected == "mix") Color.White else Color(0xFF949494)
                    )
                )
            }

        }
    }
}


@Preview
@Composable
fun StartBtnPreview() {
    StartBtn(navController = rememberNavController())}