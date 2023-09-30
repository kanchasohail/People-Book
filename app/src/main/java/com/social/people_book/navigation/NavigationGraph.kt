package com.social.people_book.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.social.people_book.ui.theme.ThemeViewModel
import com.social.people_book.util.sharedViewModel
import com.social.people_book.views.add_person_screen.AddPersonScreen
import com.social.people_book.views.auth_screen.AuthViewModel
import com.social.people_book.views.auth_screen.ForgotPasswordScreen
import com.social.people_book.views.auth_screen.LoginScreen
import com.social.people_book.views.auth_screen.SignUpScreen
import com.social.people_book.views.home_screen.HomeScreen
import com.social.people_book.views.person_details_screen.PersonDetailsScreen
import com.social.people_book.views.premium_screen.PremiumScreen
import com.social.people_book.views.settings_screen.SettingsScreen
import com.social.people_book.views.splash_screen.SplashScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    themeViewModel: ThemeViewModel,
    auth: FirebaseAuth
) {
//    NavHost(navController = navController, startDestination = Screens.SplashScreen.route) {
    NavHost(navController = navController, startDestination = Screens.HomeScreen.route) {
        //Splash Screen
        composable(Screens.SplashScreen.route) {
            SplashScreen(navController = navController, isDarkMode = isDarkMode, auth = auth)
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
                LoginScreen(isDarkMode, viewModel, navController)
            }
            //SignUp Screen
            composable(Screens.SignUpScreen.route) { entry ->
                val viewModel = entry.sharedViewModel<AuthViewModel>(navController)
                SignUpScreen(navController, viewModel, isDarkMode)
            }
            //Forgot Password Screen
            composable(Screens.ForgotPasswordScreen.route) { entry ->
                val viewModel = entry.sharedViewModel<AuthViewModel>(navController)
                ForgotPasswordScreen(isDarkMode, viewModel, navController)
            }
        }

        //Add Person Screen
        composable(Screens.AddPersonScreen.route) {
            AddPersonScreen(navController, isDarkMode)
        }

        //Person Details Screen
        composable(Screens.PersonDetailsScreen.route + "/{person_id}", arguments = listOf(
            navArgument("person_id") {
                type = NavType.StringType
            }
        )) { entry ->
            PersonDetailsScreen(
                navController = navController,
                isDarkMode = isDarkMode,
                personId = entry.arguments?.getString("person_id").toString()
            )
        }

        //Premium Screen
        composable(Screens.PremiumScreen.route) {
            PremiumScreen(navController, isDarkMode = isDarkMode)
        }

        //Settings Screen
        composable(Screens.SettingsScreen.route) {
            SettingsScreen(isDarkMode = isDarkMode, navController)
        }

    }
}