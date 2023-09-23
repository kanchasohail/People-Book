package com.social.people_book.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.google.firebase.auth.FirebaseAuth
import com.social.people_book.ui.theme.ThemeViewModel
import com.social.people_book.util.sharedViewModel
import com.social.people_book.views.add_person_screen.AddPersonScreen
import com.social.people_book.views.auth_screen.AuthViewModel
import com.social.people_book.views.auth_screen.LoginScreen
import com.social.people_book.views.auth_screen.SignUpScreen
import com.social.people_book.views.home_screen.HomeScreen
import com.social.people_book.views.person_details_screen.PersonDetailsScreen
import com.social.people_book.views.premium_screen.PremiumScreen
import com.social.people_book.views.splash_screen.SplashScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    themeViewModel: ThemeViewModel
) {
//    NavHost(navController = navController, startDestination = Screens.SplashScreen.route) {
    NavHost(navController = navController, startDestination = Screens.HomeScreen.route) {
        //Splash Screen
        composable(Screens.SplashScreen.route) {
            SplashScreen(navController = navController, isDarkMode = isDarkMode)
        }

        //Home Screen
        composable(Screens.HomeScreen.route) {
            HomeScreen(
                navController = navController,
                isDarkMode = isDarkMode,
                themeViewModel = themeViewModel
            )
        }

        //Auth Screen Navigation Group
        navigation(
            route = Screens.AuthScreen.route,
            startDestination = Screens.LoginScreen.route
        ) {
            //Login Screen
            composable(Screens.LoginScreen.route) { entry ->
                val viewModel = entry.sharedViewModel<AuthViewModel>(navController)
                LoginScreen(isDarkMode, viewModel , navController)
            }
            //SignUp Screen
            composable(Screens.SignUpScreen.route) { entry ->
                val viewModel = entry.sharedViewModel<AuthViewModel>(navController)
                SignUpScreen(navController, viewModel, isDarkMode)
            }
        }

        //Add Person Screen
        composable(Screens.AddPersonScreen.route) {
            AddPersonScreen(navController, isDarkMode)
        }

        //Person Details Screen
        composable(Screens.PersonDetailsScreen.route) {
            PersonDetailsScreen(
                isDarkMode = isDarkMode,
            )
        }

        //Premium Screen
        composable(Screens.PremiumScreen.route) {
            PremiumScreen(navController, isDarkMode = isDarkMode)
        }

    }
}