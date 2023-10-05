package com.leishmaniapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.presentation.viewmodel.ApplicationViewModel
import com.leishmaniapp.presentation.viewmodel.DiagnosisViewModel
import com.leishmaniapp.presentation.viewmodel.PatientsViewModel
import com.leishmaniapp.presentation.views.patients.AddPatientScreen
import com.leishmaniapp.presentation.views.patients.PatientDiagnosisHistoryScreen
import com.leishmaniapp.presentation.views.patients.PatientListScreen

@Composable
fun NavGraphBuilder.patientsNavGraph(
    navController: NavHostController,
    applicationViewModel: ApplicationViewModel
){
    val patientsViewModel : PatientsViewModel = hiltViewModel()
    val diagnosisViewModel : DiagnosisViewModel = hiltViewModel()


    navigation(
        route = NavigationRoutes.PatientsRoute.route,
        startDestination = NavigationRoutes.PatientsRoute.PatientList.route
    ){

        composable(NavigationRoutes.PatientsRoute.PatientList.route){
            PatientListScreen(patients = patientsViewModel.patientSet , onAddPatient = { /*TODO*/ }, onPatientClick = {patientsViewModel.patientSet.firstOrNull()  } )
        }

        composable(NavigationRoutes.PatientsRoute.AddPatient.route){
            AddPatientScreen(onCreatePatient = {patientsViewModel.patient1} )
        }


        composable(NavigationRoutes.PatientsRoute.AddPatient.route){
            AddPatientScreen(onCreatePatient = {patientsViewModel.patient1} )
        }

        composable(NavigationRoutes.PatientsRoute.PatientDiagnosisHistory.route){
            PatientDiagnosisHistoryScreen(
                patient = patientsViewModel.patient2,
                onDiagnosisClick = {},
                onDiagnosisCreate = { /*TODO*/ },
                diagnosisList = diagnosisViewModel.diagnosisList
            )
        }
    }
}