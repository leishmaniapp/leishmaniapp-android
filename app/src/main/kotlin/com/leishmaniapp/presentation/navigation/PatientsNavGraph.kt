package com.leishmaniapp.presentation.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.presentation.viewmodel.ApplicationViewModel
import com.leishmaniapp.presentation.viewmodel.DiagnosisViewModel
import com.leishmaniapp.presentation.viewmodel.PatientsViewModel
import com.leishmaniapp.presentation.ui.views.patients.AddPatientScreen
import com.leishmaniapp.presentation.ui.views.patients.PatientDiagnosisHistoryScreen
import com.leishmaniapp.presentation.ui.views.patients.PatientListScreen

fun NavGraphBuilder.patientsNavGraph(
    navController: NavHostController,
    applicationViewModel: ApplicationViewModel,
    patientsViewModel: PatientsViewModel,
    diagnosisViewModel: DiagnosisViewModel,
) {
    navigation(
        route = NavigationRoutes.PatientsRoute.route,
        startDestination = NavigationRoutes.PatientsRoute.PatientList.route
    ) {
        composable(NavigationRoutes.PatientsRoute.InitializeDiagnosis.route) {
            val context = LocalContext.current
            val patients =
                patientsViewModel.patients.collectAsStateWithLifecycle(initialValue = listOf())
            PatientListScreen(patients = patients.value.toSet(),
                onAddPatient = {
                    navController.navigateToAddPatient()
                },
                onPatientClick = { patient ->
                    // Set current patient
                    patientsViewModel.currentPatient = patient
                    // Create the new diagnosis
                    diagnosisViewModel.startNewDiagnosis(
                        context,
                        patientsViewModel.currentPatient!!,
                        applicationViewModel.specialist!!,
                        applicationViewModel.disease!!
                    )
                    // Navigate to diagnosis nav graph
                    navController.navigateToStartDiagnosis()
                })
        }

        composable(NavigationRoutes.PatientsRoute.PatientList.route) {
            val patients =
                patientsViewModel.patients.collectAsStateWithLifecycle(initialValue = listOf())
            PatientListScreen(patients = patients.value.toSet(),
                onAddPatient = {
                    navController.navigateToAddPatient()
                },
                onPatientClick = { patient ->
                    patientsViewModel.currentPatient = patient
                    navController.navigateToPatientDiagnosisHistory()
                })
        }

        composable(NavigationRoutes.PatientsRoute.AddPatient.route) {
            AddPatientScreen(onCreatePatient = { patient ->
                patientsViewModel.addNewPatient(patient)
                navController.popBackStack()
            })
        }

        composable(NavigationRoutes.PatientsRoute.PatientDiagnosisHistory.route) {
            val context = LocalContext.current
            PatientDiagnosisHistoryScreen(patient = patientsViewModel.currentPatient!!,
                onDiagnosisClick = { diagnosis ->
                    // Set the diagnosis
                    diagnosisViewModel.currentDiagnosis.value = diagnosis
                    navController.navigateToDiagnosisHistory()
                },
                onDiagnosisCreate = {
                    // Create the new diagnosis
                    diagnosisViewModel.startNewDiagnosis(
                        context,
                        patientsViewModel.currentPatient!!,
                        applicationViewModel.specialist!!,
                        applicationViewModel.disease!!
                    )
                    // Navigate to diagnosis nav graph
                    navController.navigateToStartDiagnosis()
                },
                diagnosisList = diagnosisViewModel
                    .diagnosesForPatient(patientsViewModel.currentPatient!!)
                    .filter { it.finalized && it.analyzed },
                onBackButton = { navController.popBackStack() })
        }
    }
}

fun NavHostController.navigateToPatientsNavGraph() {
    this.navigate(NavigationRoutes.PatientsRoute.route)
}

fun NavHostController.navigateToInitializeDiagnosis() {
    this.navigate(NavigationRoutes.PatientsRoute.InitializeDiagnosis.route)
}

private fun NavHostController.navigateToPatientDiagnosisHistory() {
    this.navigate(NavigationRoutes.PatientsRoute.PatientDiagnosisHistory.route)
}

private fun NavHostController.navigateToAddPatient() {
    this.navigate(NavigationRoutes.PatientsRoute.AddPatient.route)
}