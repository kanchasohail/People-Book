package com.social.people_book.navigation

sealed class Screens(val route: String) {
    object SplashScreen : Screens("splash_screen")
    object HomeScreen : Screens("home_screen")

    object AddPersonScreen:Screens("add_person_screen")
    object PersonDetailsScreen : Screens("person_details_screen")
    object PremiumScreen : Screens("premium_screen")

}