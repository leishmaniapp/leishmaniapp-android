package com.leishmaniapp.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.entities.disease.Disease
import com.leishmaniapp.presentation.views.start.AuthenticationScreen
import com.leishmaniapp.presentation.views.start.DiseasesMenuScreen
import com.leishmaniapp.presentation.views.start.GreetingsScreen

fun NavGraphBuilder.startNavGraph(
    navController: NavHostController,
    onDiseaseSelection: (Disease) -> Unit
) {
    navigation(
        route = NavigationRoutes.StartRoute.route,
        startDestination = NavigationRoutes.StartRoute.GreetingsScreen.route
    ) {
        composable(route = NavigationRoutes.StartRoute.GreetingsScreen.route) {
            GreetingsScreen(onContinue = { navController.navigateToAuthentication() })
        }

        composable(route = NavigationRoutes.StartRoute.AuthenticationRoute.route) {
            AuthenticationScreen(onAuthenticate = { username, password ->
                //TODO: Authenticate user before going to disease
                navController.navigateToDiseasesMenu()
            })
        }

        composable(route = NavigationRoutes.StartRoute.DiseasesRoute.route) {
            DiseasesMenuScreen(onDiseaseSelection = { disease ->
                // Select the disease and navigate to the main menu
                onDiseaseSelection.invoke(disease)
            })
        }
    }
}

internal fun NavController.navigateToAuthentication() {
    this.navigate(NavigationRoutes.StartRoute.AuthenticationRoute.route)
}

internal fun NavController.navigateToDiseasesMenu() {
    this.navigate(NavigationRoutes.StartRoute.DiseasesRoute.route) {
        this.popUpTo(NavigationRoutes.StartRoute.GreetingsScreen.route) {
            inclusive = true
        }
    }
}