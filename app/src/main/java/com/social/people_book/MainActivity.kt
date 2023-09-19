package com.social.people_book

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.social.people_book.navigation.NavigationGraph
import com.social.people_book.ui.theme.PeopleBookTheme
import com.social.people_book.ui.theme.ThemeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this
        setContent {
            val viewModel = viewModel<ThemeViewModel>(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ThemeViewModel(context = context) as T
                    }
                }
            )

            //Theme mode state
//            val isSystemDarkTheme = isSystemInDarkTheme()
            val isSystemDarkTheme = true
            var isDarkMode by remember {
                mutableStateOf(
                    viewModel.isDarkMode ?: isSystemDarkTheme
                )
            }

            LaunchedEffect(key1 = viewModel.isDarkMode) {
                isDarkMode = viewModel.isDarkMode ?: isSystemDarkTheme
                Log.d("DarkModeValueViewModel***", viewModel.isDarkMode.toString())
            }
            PeopleBookTheme(darkTheme = isDarkMode) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavigationGraph(
                        navController = navController,
                        isDarkMode = isDarkMode
                    )
                }
            }
        }
    }
}
