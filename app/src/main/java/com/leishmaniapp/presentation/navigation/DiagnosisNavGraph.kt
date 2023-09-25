package com.leishmaniapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.presentation.viewmodel.ApplicationViewModel
import com.leishmaniapp.presentation.viewmodel.DiagnosisViewModel
import com.leishmaniapp.presentation.views.diagnosis.DiagnosisAndAnalysisScreen
import com.leishmaniapp.presentation.views.diagnosis.DiagnosisImageGridScreen
import com.leishmaniapp.presentation.views.diagnosis.DiagnosisTableScreen
import com.leishmaniapp.presentation.views.diagnosis.DiagnosticImageEditSection
import com.leishmaniapp.presentation.views.diagnosis.DiagnosticImageSection

@Composable
fun NavGraphBuilder.diagnosisNavGraph(
    navController: NavHostController,
    applicationViewModel: ApplicationViewModel
) {
    //TODO: in startDestination change to camerax screen
    val diagnosisViewModel: DiagnosisViewModel = hiltViewModel()
    val image = diagnosisViewModel.actualImage
    val onCompletedImageEdit = false
    navigation(
        route = NavigationRoutes.DiagnosisRoute.route,
        startDestination = NavigationRoutes.DiagnosisRoute.DiagnosisTable.route
    ) {

        //TODO: camerax composable

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisTable.route) {

            DiagnosisTableScreen(
                diagnosis = diagnosisViewModel.diagnosis,
                onBackButton = { navController.popBackStack() },
                onShareDiagnosis = {/*TODO*/ })
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisAndAnalysis.route) {

            DiagnosisAndAnalysisScreen(
                diagnosis = diagnosisViewModel.diagnosis,
                image = diagnosisViewModel.actualImage,
                onImageChange = {/*TODO*/ })
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisImageGrid.route) {

            DiagnosisImageGridScreen(
                diagnosis = diagnosisViewModel.diagnosis,
                isBackground = false
            )
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosticImageEdit.route) {
            DiagnosticImageEditSection(
                modifier = Modifier,
                image = image,
                onCompleted = { image, onCompletedImageEit ->
                    /*TODO*/
                })
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosticImageSection.route) {
            DiagnosticImageSection(image = image, onImageEdit = { /*TODO*/ }) {

            }

        }
    }
}


