package com.social.people_book.navigation

sealed class Screens(val route: String) {
    object SplashScreen : Screens("splash_screen")

    object HomeRoute : Screens("home_route") // Home Screens navigation group
    object HomeScreen : Screens("home_screen")

    object SearchScreen : Screens("search_screen")

    object FavoritesScreen : Screens("favorite_screen")

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

    object AddTagScreen : Screens("add_tag_screen")


    object TrashScreenGroup : Screens("trash_screen_group") // Trash Screen Group
    object TrashScreen : Screens("trash_screen")

    object TrashPersonDetailsScreen : Screens("trash_person_details_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

}