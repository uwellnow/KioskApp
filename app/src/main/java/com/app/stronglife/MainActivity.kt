package com.app.stronglife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.navigation.NavGraph
import com.app.stronglife.ui.component.TopBar
import com.app.stronglife.ui.screen.firstScreen.FirstScreen
import com.app.stronglife.ui.screen.menuScreen.MenuScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavGraph(navController = navController) // ← 여기서 호출

        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(
    name = "1920x1080 Landscape",
    showBackground = true,
    device = "spec:width=1920px,height=1080px,dpi=81"
)

@Composable
fun GreetingPreview() {
    FirstScreen()
}