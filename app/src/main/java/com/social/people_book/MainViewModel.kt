package com.social.people_book

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.social.people_book.room_database.PersonRoom
import kotlinx.coroutines.launch

class MainViewModel(context: Context) : ViewModel() {
    private val isDarkThemeKey: String = "is_dark_theme"

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("people_book_prefs", Context.MODE_PRIVATE)
    }

    val personDao = MainActivity.db.personDao()

    private val darkModeSetting: Boolean? = isThemeDarkMode()

    var isDarkMode by mutableStateOf(
        darkModeSetting
    )


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

    fun addPerson(personRoom: PersonRoom){
        viewModelScope.launch {
            personDao.addPerson(personRoom)
        }
    }

}