package com.leishmaniapp.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.presentation.viewmodel.ApplicationViewModel
import com.leishmaniapp.presentation.views.menu.DatabaseScreen
import com.leishmaniapp.presentation.views.menu.MainMenuScreen

fun NavGraphBuilder.menuNavGraph(
    navController: NavHostController,
    applicationViewModel: ApplicationViewModel
) {
    navigation(
        route = NavigationRoutes.MenuRoute.route,
        startDestination = NavigationRoutes.MenuRoute.MainMenuRoute.route
    ) {
        composable(NavigationRoutes.MenuRoute.MainMenuRoute.route) {
            // Show the main menu screen
            MainMenuScreen(
                disease = applicationViewModel.disease!!,
                onStartDiagnosis = { /*TODO*/ },
                onPatientList = { navController.navigateToPatientsNavGraph() },
                onAwaitingDiagnoses = { /*TODO*/ },
                onDatabase = { navController.navigateToDatabase() },
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

fun NavHostController.navigateToMenu() {
    this.navigate(NavigationRoutes.MenuRoute.MainMenuRoute.route) {
        popUpTo(0) // Pop every other route
    }
}

internal fun NavHostController.navigateToDatabase() {
    this.navigate(NavigationRoutes.MenuRoute.DatabaseRoute.route)
}