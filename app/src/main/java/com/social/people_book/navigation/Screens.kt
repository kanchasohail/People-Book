package com.social.people_book.navigation

sealed class Screens(val route: String) {
    object SplashScreen : Screens("splash_screen")
    object HomeScreen : Screens("home_screen")

    object AuthScreen : Screens("auth_screen") //Auth Screens navigation group
    object LoginScreen : Screens("login_screen")
    object SignUpScreen : Screens("signup_screen")
    object ForgotPasswordScreen : Screens("forgot_password_screen")


    object AddPersonScreen : Screens("add_person_screen")

    object PersonDetailsGroup :
        Screens("person_details_group") // Person Details Screens navigation group

    object PersonDetailsScreen : Screens("person_details_screen")
    object PersonDetailsEditingScreen : Screens("person_details_editing_screen")


    object PremiumScreen : Screens("premium_screen")

    object SettingsScreen : Screens("settings_screen")

    object TrashScreen : Screens("trash_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

}