package com.social.people_book.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.firebase.auth.FirebaseAuth
import com.social.people_book.MainViewModel
import com.social.people_book.model.util.sharedViewModel
import com.social.people_book.views.add_person_screen.AddPersonScreen
import com.social.people_book.views.add_tag_screen.AddTagScreen
import com.social.people_book.views.auth_screen.AuthViewModel
import com.social.people_book.views.auth_screen.ForgotPasswordScreen
import com.social.people_book.views.auth_screen.LoginScreen
import com.social.people_book.views.auth_screen.SignUpScreen
import com.social.people_book.views.home_screen.FavoritesScreen
import com.social.people_book.views.home_screen.HomeScreen
import com.social.people_book.views.home_screen.HomeScreenViewModel
import com.social.people_book.views.home_screen.SearchScreen
import com.social.people_book.views.person_details_screen.PersonDetailsEditingScreen
import com.social.people_book.views.person_details_screen.PersonDetailsScreen
import com.social.people_book.views.person_details_screen.PersonDetailsViewModel
import com.social.people_book.views.premium_screen.PremiumScreen
import com.social.people_book.views.settings_screen.SettingsScreen
import com.social.people_book.views.splash_screen.SplashScreen
import com.social.people_book.views.trash_screen.TrashPersonDetailsScreen
import com.social.people_book.views.trash_screen.TrashScreen
import com.social.people_book.views.trash_screen.TrashScreenViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun SharedTransitionScope.NavigationGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    mainViewModel: MainViewModel,
    auth: FirebaseAuth,
) {
//    NavHost(navController = navController, startDestination = Screens.SplashScreen.route) {

    //To check if the user is loggedIn or not
    val startDestinationRoute =
        if (auth.currentUser != null) Screens.HomeRoute.route else Screens.AuthScreen.route
    NavHost(navController = navController, startDestination = startDestinationRoute) {
        //Splash Screen
        composable(Screens.SplashScreen.route) {
            SplashScreen(navController = navController, isDarkMode = isDarkMode, auth = auth)
        }

        //Home Screen Navigation Group
        navigation(route = Screens.HomeRoute.route, startDestination = Screens.HomeScreen.route) {

            //Home Screen
            composable(Screens.HomeScreen.route) { entry ->
                val viewModel = entry.sharedViewModel<HomeScreenViewModel>(navController)
                HomeScreen(
                    navController = navController,
                    animatedVisibilityScope = this,
                    isDarkMode = isDarkMode,
                    viewModel = viewModel,
                    mainViewModel = mainViewModel
                )
            }

            //Search Screen
            composable(Screens.SearchScreen.route) { entry ->
                val viewModel = entry.sharedViewModel<HomeScreenViewModel>(navController)
                SearchScreen(
                    navController = navController,
                    animatedVisibilityScope = this,
                    viewModel = viewModel,
                    isDarkMode = isDarkMode
                )
            }

            //Favorites Screen
            composable(Screens.FavoritesScreen.route) { entry ->
                val viewModel =
                    entry.sharedViewModel<HomeScreenViewModel>(navController = navController)
                FavoritesScreen(
                    navController = navController,
                    animatedVisibilityScope = this,
                    viewModel = viewModel,
                    isDarkMode = isDarkMode
                )
            }
        }


        //Auth Screen Navigation Group
        navigation(
            route = Screens.AuthScreen.route,
            startDestination = Screens.SignUpScreen.route
        ) {
            //SignUp Screen
            composable(Screens.SignUpScreen.route) { entry ->
                val viewModel = entry.sharedViewModel<AuthViewModel>(navController)
                SignUpScreen(navController, viewModel, isDarkMode)
            }
            //Login Screen
            composable(Screens.LoginScreen.route) { entry ->
                val viewModel = entry.sharedViewModel<AuthViewModel>(navController)
                LoginScreen(isDarkMode, viewModel, navController)
            }
            //Forgot Password Screen
            composable(Screens.ForgotPasswordScreen.route) { entry ->
                val viewModel = entry.sharedViewModel<AuthViewModel>(navController)
                ForgotPasswordScreen(isDarkMode, viewModel, navController)
            }
        }

        //Add Person Screen
        composable(Screens.AddPersonScreen.route) {
            AddPersonScreen(navController, isDarkMode, mainViewModel)
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
                    animatedVisibilityScope = this,
                    isDarkMode = isDarkMode,
                    personId = entry.arguments?.getString("person_id").toString(),
                    viewModel = viewModel,
                    mainViewModel = mainViewModel
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
            SettingsScreen(mainViewModel, navController)
        }

        // Add Tag Screen
        composable(Screens.AddTagScreen.route) {
            AddTagScreen(mainViewModel, navController, isDarkMode)
        }

        // Trash Navigation Group
        navigation(
            route = Screens.TrashScreenGroup.route,
            startDestination = Screens.TrashScreen.route
        ) {

            //Trash Screen
            composable(Screens.TrashScreen.route) { entry ->
                val viewModel = entry.sharedViewModel<TrashScreenViewModel>(navController)
                TrashScreen(viewModel, mainViewModel, navController, isDarkMode)
            }

            //Trash Person Details Screen
            composable(Screens.TrashPersonDetailsScreen.route) { entry ->
                val viewModel = entry.sharedViewModel<TrashScreenViewModel>(navController)

                TrashPersonDetailsScreen(
                    navController = navController,
                    animatedVisibilityScope = this,
                    isDarkMode = isDarkMode,
                    viewModel = viewModel
                )
            }
        }

    }
}