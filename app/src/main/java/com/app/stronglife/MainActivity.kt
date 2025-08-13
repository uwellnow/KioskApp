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

        sendApiKeyToServer(apiKey)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val cartViewModel: CartViewModel = viewModel()
            val productViewModel: ProductViewModel = viewModel(
                factory = ProductViewModelFactory(RetrofitClient.api)
            )
            NavGraph(navController = navController, cartViewModel = cartViewModel, productViewModel = productViewModel, apiKey)

        }
    }

    private fun sendApiKeyToServer(apiKey: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://manage-uwellnow.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        lifecycleScope.launch {
            try {
                val response = apiService.postApiKey(ApiService.ApiKeyRequest(apiKey))
                Log.d("MainActivity", "Server Response: $response")
            } catch (e: Exception) {
                Log.e("MainActivity", "Failed to send API Key", e)
            }
        }
    }
}



