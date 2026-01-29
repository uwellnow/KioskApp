package com.app.stronglife.data.remote

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(private val context: Context) {

    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun saveApiKey(apiKey: String) {
        prefs.edit().putString("api_key", apiKey).apply()
    }

    fun getApiKey(): String {
        return prefs.getString("api_key", "") ?: ""
    }

    fun hasApiKey(): Boolean {
        return prefs.contains("api_key") && getApiKey().isNotEmpty()
    }

    fun clearApiKey() {
        prefs.edit().remove("api_key").apply()
    }

    fun saveIsPicked(isPicked: Boolean) {
        prefs.edit().putBoolean("is_picked", isPicked).apply()
    }

    fun getIsPicked(): Boolean {
        return prefs.getBoolean("is_picked", false)
    }

    fun clearIsPicked() {
        prefs.edit().remove("is_picked").apply()
    }
}