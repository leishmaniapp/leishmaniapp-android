package com.leishmaniapp.presentation.navigation.graphs

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.infrastructure.camera.CameraCalibrationAnalyzer
import com.leishmaniapp.presentation.navigation.NavigationRoutes
import com.leishmaniapp.presentation.ui.dialogs.ErrorAlertDialog
import com.leishmaniapp.presentation.ui.dialogs.WillPopScopeAlertDialog
import com.leishmaniapp.presentation.ui.layout.BusyScreen
import com.leishmaniapp.presentation.ui.views.camera.CameraPermissionHandler
import com.leishmaniapp.presentation.ui.views.camera.CameraScreen
import com.leishmaniapp.presentation.ui.views.diagnosis.DiagnosisAndAnalysisScreen
import com.leishmaniapp.presentation.viewmodel.CameraViewModel
import com.leishmaniapp.presentation.viewmodel.DiagnosisViewModel
import com.leishmaniapp.presentation.viewmodel.PatientViewModel
import com.leishmaniapp.presentation.viewmodel.SessionViewModel
import com.leishmaniapp.presentation.viewmodel.state.CameraState
import com.leishmaniapp.utilities.extensions.toRecord

/**
 * Handles all the diagnosis control flow
 */
fun NavGraphBuilder.diagnosisNavGraph(
    navHostController: NavHostController,
    diagnosisViewModel: DiagnosisViewModel,
    cameraViewModel: CameraViewModel,
    sessionViewModel: SessionViewModel,
    patientViewModel: PatientViewModel,
) {
    navigation(
        route = NavigationRoutes.DiagnosisRoute.route,
        startDestination = NavigationRoutes.DiagnosisRoute.InitializeDiagnosis.route
    ) {
        composable(NavigationRoutes.DiagnosisRoute.InitializeDiagnosis.route) {

            // Get the current patient
            val patient by patientViewModel.selectedPatient.observeAsState()
            val specialist by sessionViewModel.specialist.collectAsStateWithLifecycle()

            // Trigger this code only when patient value actually changes
            LaunchedEffect(key1 = patient, key2 = specialist) {
                // Delegate to the next route
                if (patient == null) {
                    navHostController.navigateToSelectPatient()
                } else if (specialist != null) {
                    diagnosisViewModel.startDiagnosis(specialist!!.toRecord(), patient!!)
                    navHostController.navigateToPictureTake()
                }
            }

            // Show busy screen
            BusyScreen()
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisCamera.route) {

            // Show pop scope context
            val context = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current
            var willPopScope by remember { mutableStateOf(false) }

            // Get the state
            val state by cameraViewModel.cameraState.observeAsState(initial = CameraState.None)
            val disease by diagnosisViewModel.disease.observeAsState()

            // Wait for disease to become available
            if (disease == null) {
                BusyScreen()
                return@composable
            }

            CameraPermissionHandler {
                CameraScreen(
                    executor = ContextCompat.getMainExecutor(context),
                    lifecycleOwner = lifecycleOwner,
                    cameraCalibration = cameraViewModel.cameraCalibration,
                    onPictureTake = {
                        cameraViewModel.onPictureTake(it, disease!!, context)
                    },
                    onCancel = {
                        willPopScope = true
                    }
                )
            }

            when {
                // Show an error alert dialog
                state is CameraState.Error -> ErrorAlertDialog(error = (state as CameraState.Error).e) {
                    cameraViewModel.dismiss()
                }

                // Navigate to next page
                state is CameraState.Photo -> navHostController.navigateToDiagnosisAndAnalysis()

                // Show exit alert dialog
                willPopScope -> WillPopScopeAlertDialog(
                    onDismissRequest = {
                        willPopScope = false
                    },
                    onConfirmExit = {

                        // Navigate to patient selection or go to main menu
                        if (navHostController.previousBackStackEntry?.destination?.route
                            == NavigationRoutes.DiagnosisRoute.InitializeDiagnosis.route
                        ) {
                            navHostController.navigate(NavigationRoutes.MenuRoute.MainMenuRoute.route) {
                                popUpTo(NavigationRoutes.MenuRoute.MainMenuRoute.route)
                            }
                        } else {
                            navHostController.popBackStack()
                        }

                        // ! Cancel the diagnosis
                        // Dismiss the current camera state
                        cameraViewModel.dismiss()
                        // Dismiss the current patient
                        patientViewModel.dismiss()
                        // Discard the diagnosis
                        diagnosisViewModel.discard()
                    },
                )
            }

        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisAndAnalysis.route) {

            // Get the camera state
            val cameraState by cameraViewModel.cameraState.observeAsState()
            val currentDiagnosis by diagnosisViewModel.diagnosis.collectAsStateWithLifecycle()
            val currentImage by diagnosisViewModel.currentImageSample.collectAsStateWithLifecycle()

            when (cameraState) {

                // Return to picture take
                is CameraState.Error, CameraState.None -> navHostController.navigateToPictureTake {
                    popUpTo(NavigationRoutes.DiagnosisRoute.DiagnosisAndAnalysis.route)
                }

                // Show a loading screen
                null -> BusyScreen()

                // Show the analysis screen
                is CameraState.Photo -> {

                    LaunchedEffect(key1 = cameraState) {
                        // Set the image sample
                        diagnosisViewModel.setCurrentImageSample((cameraState as CameraState.Photo).location)
                    }

                    when {
                        currentDiagnosis == null || currentImage == null -> {
                            BusyScreen()
                        }

                        else -> {
                            DiagnosisAndAnalysisScreen(
                                diagnosis = currentDiagnosis!!,
                                image = currentImage!!,
                                onNextActionNotAnalyzed = {},
                                onImageChange = { image ->
                                    diagnosisViewModel.updateImageSample(image)
                                },
                                onRepeatAction = {
                                    cameraViewModel.dismiss()
                                },
                                onAnalyzeAction = { /*TODO*/ },
                                onNextAction = { /*TODO*/ },
                                onFinishAction = { /*TODO*/ },
                            )
                        }
                    }
                }
            }
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

private fun NavHostController.navigateToPictureTake(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(NavigationRoutes.DiagnosisRoute.DiagnosisCamera.route, builder)
}

private fun NavHostController.navigateToDiagnosisAndAnalysis() {
    navigate(NavigationRoutes.DiagnosisRoute.DiagnosisAndAnalysis.route)
}