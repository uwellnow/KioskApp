package com.app.stronglife.util

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LanguageManager(app: Application) : AndroidViewModel(app) {

    companion object {
        private const val PREF = "lang_prefs"
        private const val KEY_LANG = "language_tag"
        private const val DEFAULT = "ko"
    }

    private val prefs = app.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    private val _languageTag = MutableStateFlow(loadSavedLanguage())
    val languageTag: StateFlow<String> = _languageTag


    private fun loadSavedLanguage(): String {
        val saved = prefs.getString(KEY_LANG, null)
        return saved ?: DEFAULT
    }

    fun applySavedLanguage() {
        setLanguage(_languageTag.value, persist = false)
    }

    fun setLanguage(tag: String, persist: Boolean = true) {
        val locales = LocaleListCompat.forLanguageTags(tag)
        AppCompatDelegate.setApplicationLocales(locales)

        _languageTag.value = tag

        if (persist) {
            prefs.edit().putString(KEY_LANG, tag).apply() }
        }
    }
}