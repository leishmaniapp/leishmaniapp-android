package com.leishmaniapp.presentation.navigation.graphs

import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.presentation.navigation.NavigationRoutes
import com.leishmaniapp.presentation.ui.layout.BusyScreen
import com.leishmaniapp.presentation.viewmodel.PatientViewModel

/**
 * Handles all the diagnosis control flow
 */
fun NavGraphBuilder.diagnosisNavGraph(
    navHostController: NavHostController,
    patientViewModel: PatientViewModel,
) {
    navigation(
        route = NavigationRoutes.DiagnosisRoute.route,
        startDestination = NavigationRoutes.DiagnosisRoute.InitializeDiagnosis.route
    ) {
        composable(NavigationRoutes.DiagnosisRoute.InitializeDiagnosis.route) {

            // Get the current patient
            val patient by patientViewModel.selectedPatient.observeAsState()

            // Trigger this code only when patient value actually changes
            LaunchedEffect(key1 = patient) {
                // Delegate to the next route
                if (patient == null) {
                    navHostController.navigateToSelectPatient()
                } else {
                    navHostController.navigateToPictureTake()
                }
            }

            // Show busy screen
            BusyScreen()
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisCamera.route) {
            Text(text = "Pciture take!")
        }
    }
}

/**
 * Start a new diagnosis
 */
internal fun NavHostController.navigateToDiagnosisRoute(
    builder: NavOptionsBuilder.() -> Unit = {
        popUpTo(0)
    }
) {
    this.navigate(NavigationRoutes.DiagnosisRoute.route, builder)
}

private fun NavHostController.navigateToPictureTake() {
    navigate(NavigationRoutes.DiagnosisRoute.DiagnosisCamera.route)
}