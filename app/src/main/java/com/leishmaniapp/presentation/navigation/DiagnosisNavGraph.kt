package com.leishmaniapp.presentation.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.R
import com.leishmaniapp.entities.Image
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

        // Camera
        composable(NavigationRoutes.DiagnosisRoute.DiagnosisCamera.route) {
            val context = LocalContext.current
            val diagnosis by diagnosisViewModel.currentDiagnosis.collectAsState()
            CameraView(diagnosis = diagnosis!!, onCanceled = {
                // Show toast
                Toast.makeText(context, R.string.camera_exit, Toast.LENGTH_SHORT).show()
                // Return to previous
                navController.popBackStack()
            }, onPictureTake = { uri ->
                // Image Standardization
                val imageStandardizationResult = diagnosisViewModel.standardizeImage(uri)
                Log.d("ImageStandardization", "Got result = $imageStandardizationResult")

                //TODO: What if image fails?

                // Create Image Entity
                val currentDiagnosis = diagnosisViewModel.currentDiagnosis.value!!
                val newImage = Image(
                    sample = currentDiagnosis.samples,
                    size = imageStandardizationResult!!,
                    path = uri
                )

                diagnosisViewModel.currentImage.value = newImage
                navController.navigateToDiagnosisAndAnalysis()
            })
        }

        // Sample processing
        composable(NavigationRoutes.DiagnosisRoute.DiagnosisAndAnalysis.route) {
            val diagnosis by diagnosisViewModel.currentDiagnosis.collectAsState()
            val image by diagnosisViewModel.currentImage.collectAsState()

            DiagnosisAndAnalysisScreen(
                diagnosis = diagnosis!!,
                image = image!!,
                onImageChange = { editedImage ->
                    diagnosisViewModel.currentImage.value = editedImage
                },
                onAnalyzeAction = {
                    diagnosisViewModel.analyzeImage()
                },
                onFinishAction = {},
                onNextAction = {},
                onRepeatAction = {
                    navController.navigateToRepeatPictureTake()
                })
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisTable.route) {
            val diagnosis by diagnosisViewModel.currentDiagnosis.collectAsState()
            DiagnosisTableScreen(
                diagnosis = diagnosis!!,
                onBackButton = { navController.popBackStack() },
                onShareDiagnosis = { diagnosisViewModel.shareCurrentDiagnosis() })
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
    this.navigate(NavigationRoutes.DiagnosisRoute.route)
}

private fun NavHostController.navigateToDiagnosisAndAnalysis() {
    this.navigate(NavigationRoutes.DiagnosisRoute.DiagnosisAndAnalysis.route) {
        popUpTo(NavigationRoutes.DiagnosisRoute.DiagnosisCamera.route) {
            inclusive = true
        }
    }
}

private fun NavHostController.navigateToRepeatPictureTake() {
    this.navigate(NavigationRoutes.DiagnosisRoute.DiagnosisCamera.route) {
        popUpTo(NavigationRoutes.DiagnosisRoute.DiagnosisAndAnalysis.route) {
            inclusive = true
        }
    }
}


