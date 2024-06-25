package com.leishmaniapp.presentation.navigation.graphs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.R
import com.leishmaniapp.domain.exceptions.BadInputException
import com.leishmaniapp.presentation.navigation.NavigationRoutes
import com.leishmaniapp.presentation.ui.dialogs.BusyAlertDialog
import com.leishmaniapp.presentation.ui.dialogs.ErrorAlertDialog
import com.leishmaniapp.presentation.ui.dialogs.InvalidInputAlertDialog
import com.leishmaniapp.presentation.ui.dialogs.SuccessAlertDialog
import com.leishmaniapp.presentation.ui.layout.BusyScreen
import com.leishmaniapp.presentation.ui.views.patients.AddPatientScreen
import com.leishmaniapp.presentation.ui.views.patients.PatientDiagnosisHistoryScreen
import com.leishmaniapp.presentation.ui.views.patients.PatientListScreen
import com.leishmaniapp.presentation.viewmodel.PatientViewModel
import com.leishmaniapp.presentation.viewmodel.SessionViewModel
import com.leishmaniapp.presentation.viewmodel.state.PatientState

fun NavGraphBuilder.patientsNavGraph(
    navHostController: NavHostController,
    patientViewModel: PatientViewModel,
) {
    navigation(
        route = NavigationRoutes.PatientsRoute.route,
        startDestination = NavigationRoutes.PatientsRoute.PatientList.route
    ) {
        composable(NavigationRoutes.PatientsRoute.PatientList.route) {

            val patients by patientViewModel.patients.collectAsStateWithLifecycle()

            PatientListScreen(
                patients = patients.toSet(),
                onBackButton = {
                    navHostController.popBackStack()
                },
                onAddPatient = {
                    navHostController.navigateToAddPatient()
                },
                onPatientClick = { patient ->
                    patientViewModel.selectPatient(patient)
                    navHostController.navigateToPatientDiagnosisHistory()
                })
        }

        composable(NavigationRoutes.PatientsRoute.AddPatient.route) {

            val state: PatientState by patientViewModel.state.observeAsState(initial = PatientState.None)

            AddPatientScreen(
                backButtonAction = { navHostController.popBackStack() }
            ) { name, id, doctype ->
                patientViewModel.createNewPatient(name, id, doctype)
            }

            when (state) {
                PatientState.Busy -> BusyAlertDialog()

                is PatientState.Error -> if ((state as PatientState.Error).err is BadInputException) {
                    InvalidInputAlertDialog {
                        patientViewModel.dismissState()
                    }
                } else {
                    ErrorAlertDialog(error = (state as PatientState.Error).err) {
                        patientViewModel.dismissState()
                    }
                }

                PatientState.Success -> SuccessAlertDialog(
                    stringResource(id = R.string.alert_inserted_patient)
                ) {
                    navHostController.popBackStack()
                    patientViewModel.dismissState()
                }

                else -> {}
            }
        }

        composable(NavigationRoutes.PatientsRoute.PatientDiagnosisHistory.route) {

            // Get the active patient
            val patient by patientViewModel.selectedPatient.observeAsState()
            if (patient == null) {
                BusyScreen()
                return@composable
            }

            // Get the list of diagnoses associated to this patient
            val diagnoses by patientViewModel.diagnosesForSelectedPatient.collectAsStateWithLifecycle()

            PatientDiagnosisHistoryScreen(
                patient = patient!!,
                diagnosisList = diagnoses,
                onBackButton = {
                    navHostController.popBackStack()
                    patientViewModel.dismissPatient()
                },
                onDiagnosisClick = {
                    /* TODO */
                }, onDiagnosisCreate = {
                    /* TODO */
                })
        }
    }
}


internal fun NavHostController.navigateToPatientsRoute() {
    navigate(NavigationRoutes.PatientsRoute.route)
}

private fun NavHostController.navigateToAddPatient() {
    this.navigate(NavigationRoutes.PatientsRoute.AddPatient.route)
}

private fun NavHostController.navigateToPatientDiagnosisHistory() {
    this.navigate(NavigationRoutes.PatientsRoute.PatientDiagnosisHistory.route)
}