package com.social.people_book

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.social.people_book.model.room_database.PersonDatabase
import com.social.people_book.navigation.NavigationGraph
import com.social.people_book.ui.theme.PeopleBookTheme

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var db: PersonDatabase
    }

    private lateinit var auth: FirebaseAuth

    @OptIn(ExperimentalSharedTransitionApi::class)
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context: Context = this

        // Initialize Firebase Auth
        auth = Firebase.auth
        db = PersonDatabase.getInstance(context)

        setContent {
            val viewModel = viewModel<MainViewModel>(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return MainViewModel(context = context) as T
                    }
                })
            val isSystemDark = isSystemInDarkTheme()
            val darkMode by remember {
                derivedStateOf {
                    viewModel.isDarkMode.value ?: isSystemDark
                }
            }

            PeopleBookTheme(viewModel) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    SharedTransitionLayout {
                        NavigationGraph(
                            navController = navController,
                            isDarkMode = darkMode,
                            mainViewModel = viewModel,
                            auth = auth,
                        )
                    }
                }
            }
        }
    }
}