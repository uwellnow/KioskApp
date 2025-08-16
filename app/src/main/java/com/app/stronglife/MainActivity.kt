package com.app.stronglife

import CartViewModel
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.data.remote.ApiService
import com.app.stronglife.data.remote.PrefsManager
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.navigation.NavGraph
import com.app.stronglife.viewmodel.ProductViewModel
import com.app.stronglife.viewmodel.ProductViewModelFactory
import com.app.stronglife.viewmodel.UserCodeViewModel
import com.app.stronglife.viewmodel.UserCodeViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefsManager = PrefsManager(this)
        prefsManager.saveApiKeyIfNotExists()
        val apiKey = prefsManager.getApiKey()
        Log.d("API_KEY", apiKey)

        // 앱 시작 시 API 키를 서버에 전송
        val userCodeViewModel = UserCodeViewModel.getInstance(RetrofitClient.api)
        userCodeViewModel.sendApiKey(apiKey)

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
                apiKey = apiKey)

        }
    }

}



