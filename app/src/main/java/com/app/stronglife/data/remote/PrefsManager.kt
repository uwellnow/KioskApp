package com.app.stronglife.data.remote

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings

class PrefsManager(private val context: Context) {

    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun saveApiKeyIfNotExists() {
        if (!prefs.contains("api_key")) {
            val deviceId = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
            prefs.edit().putString("api_key", deviceId).apply()
        }
    }

    fun getApiKey(): String {
        return prefs.getString("api_key", "") ?: ""
    }
}