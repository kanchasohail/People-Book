package com.social.people_book

import android.content.Context
import android.os.Bundle
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.social.people_book.navigation.NavigationGraph
import com.social.people_book.navigation.Screens
import com.social.people_book.ui.theme.PeopleBookTheme
import com.social.people_book.ui.theme.ThemeViewModel

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = Firebase.auth
        val context: Context = this

        setContent {
            val viewModel = viewModel<ThemeViewModel>(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ThemeViewModel(context = context) as T
                    }
                }
            )

            //Theme mode state
            val isSystemDarkTheme = isSystemInDarkTheme()
            var isDarkMode by remember {
                mutableStateOf(
                    viewModel.isDarkMode ?: isSystemDarkTheme
                )
            }

            LaunchedEffect(key1 = viewModel.isDarkMode) {
                isDarkMode = viewModel.isDarkMode ?: isSystemDarkTheme
            }

            PeopleBookTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavigationGraph(
                        navController = navController,
                        isDarkMode = isDarkMode,
                        themeViewModel = viewModel,
                        auth = auth
                    )
                    if(auth.currentUser == null){
                        navController.navigate(Screens.AuthScreen.route)
                    }
                }
            }
        }
    }
}
