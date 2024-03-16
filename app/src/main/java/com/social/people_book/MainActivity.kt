package com.social.people_book

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.social.people_book.model.other.BottomNavigationItemModel
import com.social.people_book.navigation.NavigationGraph
import com.social.people_book.navigation.Screens
import com.social.people_book.room_database.PersonDatabase
import com.social.people_book.ui.theme.PeopleBookTheme

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var db: PersonDatabase
    }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = Firebase.auth
        db = Room.databaseBuilder(applicationContext, PersonDatabase::class.java, "people_book.db").build()
        val context: Context = this

        setContent {
            val viewModel = viewModel<MainViewModel>(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return MainViewModel(context = context) as T
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
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val items = listOf(
                        BottomNavigationItemModel(
                            Icons.Rounded.Home, Screens.HomeScreen.route, "Home"
                        ),
                        BottomNavigationItemModel(
                            Icons.Rounded.Settings, Screens.SettingsScreen.route, "Settings"
                        )
                    )
                    Scaffold(bottomBar = {
                        if (navController.currentBackStackEntryAsState().value?.destination?.route in listOf(
                                Screens.HomeScreen.route,
                                Screens.SettingsScreen.route,
                            )
                        ) {
                            NavigationBar(modifier = Modifier.height(50.dp)) {
                                items.forEach { item ->
                                    NavigationBarItem(
                                        selected = navBackStackEntry?.destination?.route == item.route,
                                        onClick = {
                                            navController.navigate(item.route) {
                                                navController.popBackStack(
                                                    navController.graph.findStartDestination().id,
                                                    inclusive = false,
                                                    saveState = true
                                                )
                                                launchSingleTop = true
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = item.icon,
                                                contentDescription = item.routeName
                                            )
                                        })
                                }
                            }
                        }
                    }) { paddingValues ->
                        Box(modifier = Modifier.padding(paddingValues)){
                            NavigationGraph(
                                navController = navController,
                                isDarkMode = isDarkMode,
                                mainViewModel = viewModel,
                                auth = auth,
                            )
                        }
                    }
                }
            }
        }
    }
}
