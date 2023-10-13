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
                onStartDiagnosis = { navController.navigateToInitializeDiagnosis() },
                onPatientList = { navController.navigateToPatientsNavGraph() },
                onAwaitingDiagnoses = { TODO("Missing navigation graph") },
                onDatabase = { navController.navigateToDatabase() },
            )
        }

        composable(NavigationRoutes.MenuRoute.DatabaseRoute.route) {
            DatabaseScreen(
                onExit = { navController.popBackStack() },
                onImport = { TODO("Import not supported yet") },
                onExport = { TODO("Export not supported yet") })
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