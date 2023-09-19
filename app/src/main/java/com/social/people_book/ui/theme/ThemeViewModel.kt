package com.social.people_book.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ThemeViewModel(context: Context) : ViewModel() {
    private val isDarkThemeKey: String = "is_dark_theme"

//    private val darkModeSetting: Boolean? = isThemeDarkMode()
    private val darkModeSetting: Boolean? = null

    var isDarkMode by mutableStateOf(
        darkModeSetting
    )


    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("people_book_prefs", Context.MODE_PRIVATE)
    }

    fun setThemeMode(isDarkMode: Boolean) {
        val stringValue = if (isDarkMode) "true" else "false"
        prefs.edit().putString(isDarkThemeKey, stringValue).apply()
    }

    private fun isThemeDarkMode(): Boolean? {
        return when (prefs.getString(isDarkThemeKey, null)) {
            "true" -> true
            "false" -> false
            else -> null
        }
    }

}