package com.app.stronglife.ui.screen.menuScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.stronglife.mock.sampleProducts
import com.app.stronglife.ui.component.TopBar
import java.nio.file.WatchEvent

@Composable
fun MenuScreen() {
    val density = LocalDensity.current
    val spacertoDp = with(density) {80f.toDp()}
    Box {
        Column (
            modifier = Modifier
        ){
            TopBar(step = 2, listOf("섭취시점 선택", "메뉴선택"))
            Spacer(modifier = Modifier.height(spacertoDp))
            ProductCard(sampleProducts)
        }
    }


}


@Composable
@Preview(
    name = "1920x1080 Landscape",
    showBackground = true,
    device = "spec:width=1920px,height=1080px,dpi=81"
)
fun MenuScreenPreview() {
    MenuScreen()
}