package com.social.people_book

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.social.people_book.ui.theme.ThemeMode

class MainViewModel(context: Context) : ViewModel() {

    private val isDarkThemeKey: String = "is_dark_theme"
    private val themeModeKey: String = "theme_mode"

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("people_book_prefs", Context.MODE_PRIVATE)
    }

    val personDao = MainActivity.db.personDao()

    var isDropDownOpen by mutableStateOf(false)

    private val _themeModeSetting = when (prefs.getString(themeModeKey, null)) {
        "light" -> ThemeMode.Light
        "dark" -> ThemeMode.Dark
        "system" -> ThemeMode.System
//        "creamy" -> ThemeMode.Creamy
//        "mint" -> ThemeMode.Mint
        else -> ThemeMode.System
    }
    var themeMode = mutableStateOf(_themeModeSetting)

    private val _darkModeSetting =
        when (prefs.getString(isDarkThemeKey, null)) {
            "true" -> true
            "false" -> false
            else -> null
        }

    var isDarkMode = mutableStateOf(_darkModeSetting)


//    fun setThemeMode(isDarkMode: Boolean) {
//        val stringValue = if (isDarkMode) "true" else "false"
//        prefs.edit().putString(isDarkThemeKey, stringValue).apply()
//        this.isDarkMode.value = isDarkMode
//    }


    fun setThemeMode(themeMode: ThemeMode) {
        val stringValue = when (themeMode) {
            ThemeMode.Light -> "light"
            ThemeMode.Dark -> "dark"
            ThemeMode.System -> "system"
//            ThemeMode.Creamy -> "creamy"
//            ThemeMode.Mint -> "mint"
        }
        prefs.edit().putString(themeModeKey, stringValue).apply()
        this.themeMode.value = themeMode
    }

}