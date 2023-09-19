package com.leishmaniapp.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.presentation.views.menu.MainMenuScreen

fun NavHostController.navigateToMenu() {
    this.navigate(NavigationRoutes.MenuRoute.MainMenuRoute.route) {
        popUpTo(0) // Pop every other route
    }
}

fun NavGraphBuilder.menuNavGraph(navController: NavHostController) {
    navigation(
        route = NavigationRoutes.MenuRoute.route,
        startDestination = NavigationRoutes.MenuRoute.MainMenuRoute.route
    ) {
        composable(NavigationRoutes.MenuRoute.MainMenuRoute.route) {
            // Show the main menu screen
            MainMenuScreen()
        }
    }
}