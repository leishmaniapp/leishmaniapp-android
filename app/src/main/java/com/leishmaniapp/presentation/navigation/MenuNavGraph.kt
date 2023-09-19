package com.leishmaniapp.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.presentation.views.menu.DatabaseScreen
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
            MainMenuScreen(
                onStartDiagnosis = { /*TODO*/ },
                onPatientList = { /*TODO*/ },
                onDatabase = { navController.navigateToDatabase() },
                onAwaitingDiagnoses = { /*TODO*/ }
            )
        }

        composable(NavigationRoutes.MenuRoute.DatabaseRoute.route) {
            DatabaseScreen(
                onExit = { navController.popBackStack() },
                onImport = { /*TODO*/ },
                onExport = { /*TODO*/ })
        }
    }
}

internal fun NavHostController.navigateToDatabase() {
    this.navigate(NavigationRoutes.MenuRoute.DatabaseRoute.route)
}