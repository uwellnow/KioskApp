package com.app.stronglife

import CartViewModel
import android.os.Bundle
import android.util.Log
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.data.remote.ApiService
import com.app.stronglife.data.remote.PrefsManager
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.navigation.NavGraph
import com.app.stronglife.util.LanguageManager
import com.app.stronglife.viewmodel.ProductViewModel
import com.app.stronglife.viewmodel.ProductViewModelFactory
import com.app.stronglife.viewmodel.UserCodeViewModel
import com.app.stronglife.viewmodel.UserCodeViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var languageManager: LanguageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        languageManager = LanguageManager(application)
        languageManager.applySavedLanguage()

        val prefsManager = PrefsManager(this)

        prefsManager.clearApiKey()
        
        var apiKey by mutableStateOf(prefsManager.getApiKey())
        Log.d("API_KEY", "Current API Key: $apiKey")

        if (prefsManager.hasApiKey()) {
            val userCodeViewModel = UserCodeViewModel.getInstance(RetrofitClient.api)
            userCodeViewModel.sendApiKey(apiKey)
        }

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val cartViewModel: CartViewModel = viewModel()
            val productViewModel: ProductViewModel = viewModel(
                factory = ProductViewModelFactory(RetrofitClient.api)
            )
            val userViewModel: UserCodeViewModel = viewModel(
                factory = UserCodeViewModelFactory(RetrofitClient.api)
            )
            
            NavGraph(
                navController = navController,
                cartViewModel = cartViewModel,
                productViewModel = productViewModel,
                userViewModel = userViewModel,
                apiKey = apiKey,
                onApiKeyChanged = { newApiKey ->
                    apiKey = newApiKey
                }
            )
        }
    }
    
    override fun onResume() {
        super.onResume()
        // 네비게이션 바 숨기기 (Android 11+)
        hideNavigationBar()
    }
    
    private fun hideNavigationBar() {
        // Android 11 (API 30) 이상용 - 안전한 방법
        try {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let { controller ->
                controller.hide(android.view.WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } catch (e: Exception) {
            // 에러 발생 시 무시
            android.util.Log.w("MainActivity", "Failed to hide navigation bar: ${e.message}")
        }
    }
}



