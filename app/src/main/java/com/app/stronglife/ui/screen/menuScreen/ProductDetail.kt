package com.app.stronglife.ui.screen.menuScreen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app.stronglife.R
import com.app.stronglife.ui.theme.black
import com.app.stronglife.ui.theme.descGray
import com.app.stronglife.ui.theme.lightGray
import com.app.stronglife.ui.theme.lightRed
import com.app.stronglife.ui.theme.midGray
import com.app.stronglife.ui.theme.superLightGray

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProductDetail (
    image:String, 
    title:String, 
    desc:String, 
    nut:String,
    nut_eng:String,
    isSoldOut: Boolean = false,
    onClose: () -> Unit, 
    onAddToCart: () -> Unit, 
    onGoCart: () -> Unit,
    languageManager: com.app.stronglife.util.LanguageManager
) {
    val density = LocalDensity.current
    val widthtoDp = with(density) {1610f.toDp()}
    val heighttoDp = with(density) {776.toDp()}
    val titletoSp = with(density) {40f.toSp()}
    val desctoSp = with(density) {28f.toSp()}
    val imagetoDp = with(density) {480f.toDp()}
    val roundtoDp = with(density) {20f.toDp()}
    val imagetoTextDp = with(density) {49f.toDp()}
    val blurRadiusPx = with(density) { 24.dp.toPx() }
    val spacertoDp = with(density) {40f.toDp()}
    val space2Dp = with(density) {43f.toDp()}
    val space3Dp = with(density) { 24f.toDp()}
    val smalltextSp = with(density) {20f.toSp()}

    val allNutrientsKo = parseNutritionInfo(nut)
    val funNutrientsEng = parseNutritionInfo(nut_eng)
    val functionalKo = filterFunctionalNutrients(allNutrientsKo)
    val functionalEng = filterFunctionalNutrients(funNutrientsEng)
    var showDialog by remember { mutableStateOf(false) }

    val langTag by languageManager.languageTag.collectAsState()
    val nutrientsToShow = if (langTag == "ko") functionalKo else functionalEng

    if (showDialog) {
        NutritionDialog(
            nutritions = parseNutritionInfo(nut),
            onDismiss = { showDialog = false }
        )
        return
    }

    Row (
        modifier = Modifier
            .width(widthtoDp)
            .height(heighttoDp)
            .drawBehind {
                drawIntoCanvas { canvas ->
                    val paint = Paint().asFrameworkPaint().apply {
                        color = lightRed.toArgb()
                        maskFilter = android.graphics.BlurMaskFilter(blurRadiusPx, android.graphics.BlurMaskFilter.Blur.NORMAL)
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
            .background(Color.White, RoundedCornerShape(roundtoDp))
    ){
        Column (
            modifier = Modifier.weight(1f).fillMaxHeight().padding(spacertoDp),
        ){

            Column(
                modifier = Modifier.padding(start = imagetoTextDp, top = imagetoTextDp * 2)
            ) {
                Text(
                    text = title.replace("\\n", " ").replace("\n", " "),
                    style = TextStyle(
                        fontSize = titletoSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        fontWeight = FontWeight.Bold,
                        color = black
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = desc.replace("\\n", "\n"),
                    style = TextStyle(
                        fontSize = desctoSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontWeight = FontWeight.Normal,
                        color = lightGray
                    )
                )

                Spacer(modifier = Modifier.height(space2Dp))

                Text(
                    text = "함유 성분",
                    style = TextStyle(
                        fontSize = desctoSp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontWeight = FontWeight.Normal,
                        color = lightGray
                    )
                )

                Spacer(modifier = Modifier.height(space3Dp))

                NutritionCardRow(nutrientsToShow, lang = langTag)
            }

            Spacer(modifier = Modifier.weight(1f))

            // 하단 고정 버튼
            MenuScreenBtn(
                onBackClick = onClose,
                onCartClick = {
                    if (!isSoldOut) {
                        onAddToCart()
                        onClose()
                        onGoCart()
                    }
                },
                isCartEnabled = !isSoldOut
            )
        }

        AsyncImage(
            model = image.ifBlank { null },
            contentDescription = title,
            modifier = Modifier.fillMaxHeight()
                .clip(RoundedCornerShape(topEnd = roundtoDp, bottomEnd = roundtoDp))
        )

    }
}

