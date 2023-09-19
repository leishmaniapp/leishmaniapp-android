package com.leishmaniapp.presentation.navigation

/**
 * Navigation routes
 */
sealed class NavigationRoutes(val route: String) {
    data object StartRoute : NavigationRoutes("/start") {
        data object GreetingsScreen : NavigationRoutes(this.route + "/greetings")
        data object AuthenticationRoute : NavigationRoutes(this.route + "/authentication")
        data object DiseasesRoute : NavigationRoutes(this.route + "/diseases")
    }

    data object MenuRoute : NavigationRoutes("/menu") {
        data object MainMenuRoute : NavigationRoutes(this.route + "/main")
    }

    data object PatientsRoute : NavigationRoutes("/patients")
    data object DiagnosisRoute : NavigationRoutes("/diagnosis")
}
