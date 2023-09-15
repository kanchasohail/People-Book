package com.social.people_book.navigation

sealed class Screens(val route: String) {
    object SplashScreen : Screens("splash_screen")
    object HomeScreen : Screens("home_screen")
    object PremiumScreen : Screens("premium_screen")

}