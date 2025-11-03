package com.app.stronglife

import CartViewModel
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.data.remote.ApiService
import com.app.stronglife.data.remote.PollingService
import com.app.stronglife.data.remote.PrefsManager
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.navigation.NavGraph
import com.app.stronglife.util.LanguageManager
import com.app.stronglife.util.SystemStatusManager
import com.app.stronglife.viewmodel.ProductViewModel
import com.app.stronglife.viewmodel.ProductViewModelFactory
import com.app.stronglife.viewmodel.UserCodeViewModel
import com.app.stronglife.viewmodel.UserCodeViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

class MainActivity : ComponentActivity() {
    private lateinit var languageManager: LanguageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefsManager = PrefsManager(this)
        prefsManager.clearApiKey()
        
        var apiKey by mutableStateOf(prefsManager.getApiKey())

        if (prefsManager.hasApiKey()) {
            val userCodeViewModel = UserCodeViewModel.getInstance(RetrofitClient.api)
            userCodeViewModel.sendApiKey(apiKey)
        }

        enableEdgeToEdge()
        setContent {
            var errorDialogMessage by rememberSaveable { mutableStateOf<String?>(null) }
            val navController = rememberNavController()
            val cartViewModel: CartViewModel = viewModel()
            val productViewModel: ProductViewModel = viewModel(
                factory = ProductViewModelFactory(RetrofitClient.api)
            )
            val userViewModel: UserCodeViewModel = viewModel(
                factory = UserCodeViewModelFactory(RetrofitClient.api)
            )

            val languageManager: LanguageManager = viewModel()
            if (savedInstanceState == null) {
                languageManager.applySavedLanguage()
            }

            val lang by languageManager.languageTag.collectAsState()

            key(lang) {
                val context = LocalContext.current
                val config = Configuration(context.resources.configuration)
                config.setLocale(Locale(lang))
                val localizedContext = context.createConfigurationContext(config)

                CompositionLocalProvider(
                    LocalContext provides localizedContext
                ) {
                    NavGraph(
                        navController = navController,
                        cartViewModel = cartViewModel,
                        productViewModel = productViewModel,
                        userViewModel = userViewModel,
                        apiKey = apiKey,
                        onApiKeyChanged = { newApiKey ->
                            apiKey = newApiKey
                            prefsManager.saveApiKey(newApiKey)
                        },
                        languageManager = languageManager
                    )

                    if (errorDialogMessage != null) {
                        AlertDialog(
                            onDismissRequest = {
                                errorDialogMessage = null
                            },
                            title = { Text("시스템 알림") },
                            text = { Text(errorDialogMessage!!)},
                            confirmButton = {
                                TextButton(onClick = {
                                    navController.popBackStack()
                                }) {
                                    Text("확인")
                                }
                            }
                        )
                    }

                    LaunchedEffect(Unit) {
                        SystemStatusManager.statusFlow.collectLatest { status ->
                            if (status?.isActive == true) {
                                errorDialogMessage = "시스템 점검 중입니다: ${status.statusType}"
                            } else {
                                errorDialogMessage = null
                            }
                        }
                    }
                }
            }
        }

        startPollingService()
    }

    private fun startPollingService() {
        val serviceIntent = Intent(this, PollingService::class.java)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
    }

    private fun hideNavigationBar() {
        try {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let { controller ->
                controller.hide(android.view.WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } catch (e: Exception) {
            Log.w("MainActivity", "Failed to hide navigation bar: ${e.message}")
        }
    }
}



