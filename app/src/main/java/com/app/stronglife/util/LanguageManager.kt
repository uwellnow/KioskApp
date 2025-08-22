package com.app.stronglife.util

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

class LanguageManager(app: Application) : AndroidViewModel(app) {

    companion object {
        private const val PREF = "lang_prefs"
        private const val KEY_LANG = "language_tag"
        private const val DEFAULT = "ko"
        private const val TAG = "LanguageManager"
    }

    private val prefs = app.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    private val _languageTag = MutableStateFlow(loadSavedLanguage())
    val languageTag: StateFlow<String> = _languageTag


    private fun loadSavedLanguage(): String {
        return prefs.getString(KEY_LANG, null) ?: DEFAULT
    }

    fun applySavedLanguage() {
        setLanguage(_languageTag.value, persist = false)
    }

    fun setLanguage(tag: String, persist: Boolean = true) {
        Log.d(TAG, "setLanguage: tag=$tag, persist=$persist")

        val locale = Locale(tag)
        Locale.setDefault(locale)

        val config = Configuration(getApplication<Application>().resources.configuration)
        config.setLocale(locale)
        getApplication<Application>().resources.updateConfiguration(
            config,
            getApplication<Application>().resources.displayMetrics
        )

        _languageTag.value = tag

        if (persist) {
            prefs.edit().putString(KEY_LANG, tag).apply()
            Log.d(TAG, "setLanguage: saved=$tag in SharedPreferences")
        }
    }
}
