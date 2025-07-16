package com.app.stronglife.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.stronglife.R
import com.app.stronglife.ui.screen.firstScreen.NumberCircleWithText
import com.app.stronglife.ui.theme.mainRed

@Composable
fun TopBar(step:Int, pageNames:List<String>, navController: NavController) {
    val density = LocalDensity.current
    val heightInDp = with(density) { 120f.toDp() }
    val paddingInDp = with(density) {80f.toDp()}
    val textInSp = with(density) {36f.toSp()}
    val heightPaddingInDp = with(density) {36f.toDp()}
    val btnInDp = with(density) {43f.toDp()}

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(heightInDp)
            .background(mainRed)
            .padding(horizontal = paddingInDp, vertical = heightPaddingInDp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            pageNames.forEachIndexed { index, title ->
                if (index > 0) Spacer(modifier = Modifier.width(50.dp))
                NumberCircleWithText(
                    number = (index + 1).toString(),
                    title = title,
                    isActive = (index == pageNames.lastIndex),
                    textSizeSp = 36f
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable{navController.navigate("first")}){
            Text(
                text = "처음으로",
                style = TextStyle(
                    fontSize = textInSp,
                    fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                modifier = Modifier.size(btnInDp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_homebtn),
                contentDescription = "처음으로 이동 버튼",
                tint = Color.White
            )
        }
    }
}



