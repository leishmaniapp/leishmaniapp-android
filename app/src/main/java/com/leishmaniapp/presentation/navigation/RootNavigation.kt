package com.leishmaniapp.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.leishmaniapp.presentation.viewmodel.ApplicationViewModel

const val TAG = "RootNavigation";

@Composable
fun RootNavigation(
    navigationController: NavHostController = rememberNavController(),
    applicationViewModel: ApplicationViewModel = viewModel()
) = NavHost(
    navController = navigationController,
    startDestination = NavigationRoutes.StartRoute.route
) {

    this.startNavGraph(navigationController) { disease ->
        Log.d(TAG, "Selected disease: $disease")
        applicationViewModel.disease = disease
        navigationController.navigateToMenu()
    }

    this.menuNavGraph(navigationController)
}