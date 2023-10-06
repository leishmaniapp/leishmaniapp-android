package com.leishmaniapp.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.presentation.viewmodel.ApplicationViewModel
import com.leishmaniapp.presentation.viewmodel.DiagnosisViewModel
import com.leishmaniapp.presentation.viewmodel.PatientsViewModel
import com.leishmaniapp.presentation.views.patients.PatientDiagnosisHistoryScreen
import com.leishmaniapp.presentation.views.patients.PatientListScreen

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

        composable(NavigationRoutes.PatientsRoute.PatientList.route) {
            PatientListScreen(patients = patientsViewModel.patients,
                onAddPatient = { TODO("Create new patient") },
                onPatientClick = { patient ->
                    patientsViewModel.currentPatient = patient
                    navController.navigateToPatientDiagnosisHistory()
                })
        }

        composable(NavigationRoutes.PatientsRoute.AddPatient.route) {
//            AddPatientScreen(onCreatePatient = { patientsViewModel.patient1 })
        }

        composable(NavigationRoutes.PatientsRoute.PatientDiagnosisHistory.route) {
            PatientDiagnosisHistoryScreen(patient = patientsViewModel.currentPatient!!,
                onDiagnosisClick = { diagnosis ->
                    // Set the diagnosis
                    diagnosisViewModel.currentDiagnosis.value = diagnosis
                    navController.navigateToDiagnosisHistory()
                },
                onDiagnosisCreate = {
                    // Create the new diagnosis
                    diagnosisViewModel.startNewDiagnosis(
                        patientsViewModel.currentPatient!!,
                        applicationViewModel.specialist!!,
                        applicationViewModel.disease!!
                    )
                    // Navigate to diagnosis nav graph
                    navController.navigateToStartDiagnosis()
                },
                diagnosisList = diagnosisViewModel.diagnosesForPatient(patientsViewModel.currentPatient!!),
                onBackButton = { navController.popBackStack() })
        }
    }
}

fun NavHostController.navigateToPatientsNavGraph() {
    this.navigate(NavigationRoutes.PatientsRoute.route)
}

internal fun NavHostController.navigateToPatientDiagnosisHistory() {
    this.navigate(NavigationRoutes.PatientsRoute.PatientDiagnosisHistory.route)
}