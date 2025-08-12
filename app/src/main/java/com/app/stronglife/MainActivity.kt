package com.app.stronglife

import CartViewModel
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.app.stronglife.data.remote.PrefsManager
import com.app.stronglife.data.remote.RetrofitClient
import com.app.stronglife.navigation.NavGraph
import com.app.stronglife.viewmodel.ProductViewModel
import com.app.stronglife.viewmodel.ProductViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val prefsManager = PrefsManager(this)
        prefsManager.saveApiKeyIfNotExists()
        val apiKey = prefsManager.getApiKey()
        Log.d("API_KEY", apiKey)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val cartViewModel: CartViewModel = viewModel()
            val productViewModel: ProductViewModel = viewModel(
                factory = ProductViewModelFactory(RetrofitClient.api)
            )
            NavGraph(navController = navController, cartViewModel = cartViewModel, productViewModel = productViewModel)

        }
    }
}



