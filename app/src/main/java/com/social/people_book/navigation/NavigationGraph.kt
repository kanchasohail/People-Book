package com.social.people_book.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.social.people_book.views.person_details_screen.PersonDetailsEditingScreen
import com.social.people_book.views.person_details_screen.PersonDetailsScreen
import com.social.people_book.views.person_details_screen.PersonDetailsViewModel
import com.social.people_book.views.premium_screen.PremiumScreen
import com.social.people_book.views.settings_screen.SettingsScreen
import com.social.people_book.views.splash_screen.SplashScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    themeViewModel: ThemeViewModel,
    auth: FirebaseAuth,
    modifier: Modifier = Modifier
) {
//    NavHost(navController = navController, startDestination = Screens.SplashScreen.route) {

    //To check if the user is loggedIn or not
    val startDestinationRoute = if (auth.currentUser != null) Screens.HomeScreen.route else Screens.AuthScreen.route
    NavHost(navController = navController, startDestination = startDestinationRoute) {
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

        //Persons Details Navigation Group
        navigation(
            route = Screens.PersonDetailsGroup.route + "/{person_id}",
            startDestination = Screens.PersonDetailsScreen.route
        ) {
            //Person Details Screen
            composable(Screens.PersonDetailsScreen.route, arguments = listOf(
                navArgument("person_id") {
                    type = NavType.StringType
                }
            )) { entry ->
                val viewModel = entry.sharedViewModel<PersonDetailsViewModel>(navController)
                PersonDetailsScreen(
                    navController = navController,
                    isDarkMode = isDarkMode,
                    personId = entry.arguments?.getString("person_id").toString(),
                    viewModel = viewModel
                )
            }

            //Person Details Editing Screen
            composable(Screens.PersonDetailsEditingScreen.route) { entry ->
                val viewModel = entry.sharedViewModel<PersonDetailsViewModel>(navController)
                PersonDetailsEditingScreen(navController, isDarkMode, viewModel)
            }
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