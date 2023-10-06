package com.leishmaniapp.presentation.navigation

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.presentation.viewmodel.ApplicationViewModel
import com.leishmaniapp.presentation.viewmodel.DiagnosisViewModel
import com.leishmaniapp.presentation.views.diagnosis.CameraView
import com.leishmaniapp.presentation.views.diagnosis.DiagnosisAndAnalysisScreen
import com.leishmaniapp.presentation.views.diagnosis.DiagnosisTableScreen


fun NavGraphBuilder.diagnosisNavGraph(
    navController: NavHostController,
    applicationViewModel: ApplicationViewModel,
    diagnosisViewModel: DiagnosisViewModel,
) {
    navigation(
        route = NavigationRoutes.DiagnosisRoute.route,
        startDestination = NavigationRoutes.DiagnosisRoute.DiagnosisCamera.route
    ) {

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisCamera.route) {
            val diagnosis by diagnosisViewModel.currentDiagnosis.collectAsState()
            CameraView(diagnosis = diagnosis!!, onCanceled = {
                //TODO!
            }, onPictureTake = { uri ->
                Log.d("PICTURETAKEEE", uri.toString())

                // TODO: Generate image
                diagnosisViewModel.currentImage.value = MockGenerator.mockImage()
                navController.navigateToDiagnosisAndAnalysis()
            })
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisTable.route) {
            val diagnosis by diagnosisViewModel.currentDiagnosis.collectAsState()
            DiagnosisTableScreen(
                diagnosis = diagnosis!!,
                onBackButton = { navController.popBackStack() },
                onShareDiagnosis = { diagnosisViewModel.shareCurrentDiagnosis() })
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisAndAnalysis.route) {
            val diagnosis by diagnosisViewModel.currentDiagnosis.collectAsState()
            val image by diagnosisViewModel.currentImage.collectAsState()

            DiagnosisAndAnalysisScreen(
                diagnosis = diagnosis!!,
                image = image!!,
                onImageChange = { editedImage ->
                    diagnosisViewModel.currentImage.value = editedImage
                })
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisImageGrid.route) {
// TODO
//            DiagnosisImageGridScreen(
//                diagnosis = diagnosisViewModel.diagnosis,
//                isBackground = false
//            )
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosticImageEdit.route) {
// TODO
//            DiagnosticImageEditSection(
//                modifier = Modifier,
//                image = image,
//                onCompleted = { image, onCompletedImageEit ->
//                    /*TODO*/
//                })
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosticImageSection.route) {
// TODO
//            DiagnosticImageSection(image = image, onImageEdit = { /*TODO*/ }) {
//
//            }
        }
    }
}

fun NavHostController.navigateToDiagnosisHistory() {
    this.navigate(NavigationRoutes.DiagnosisRoute.DiagnosisTable.route)
}

fun NavHostController.navigateToStartDiagnosis() {
    this.navigate(NavigationRoutes.DiagnosisRoute.route) {
        popUpTo(0)
    }
}

internal fun NavHostController.navigateToDiagnosisAndAnalysis() {
    this.navigate(NavigationRoutes.DiagnosisRoute.DiagnosisAndAnalysis.route)
}


