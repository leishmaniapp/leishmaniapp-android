package com.leishmaniapp.presentation.navigation.graphs

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.BuildConfig
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.presentation.navigation.NavigationRoutes
import com.leishmaniapp.presentation.ui.dialogs.BackgroundDiagnosisAlertDialog
import com.leishmaniapp.presentation.ui.dialogs.BusyAlertDialog
import com.leishmaniapp.presentation.ui.dialogs.ErrorAlertDialog
import com.leishmaniapp.presentation.ui.dialogs.FinalizeDiagnosisAlertDialog
import com.leishmaniapp.presentation.ui.dialogs.WillPopCameraAlertDialog
import com.leishmaniapp.presentation.ui.layout.BusyScreen
import com.leishmaniapp.presentation.ui.views.camera.CameraPermissionHandler
import com.leishmaniapp.presentation.ui.views.camera.CameraScreen
import com.leishmaniapp.presentation.ui.views.diagnosis.AwaitingDiagnosesScreen
import com.leishmaniapp.presentation.ui.views.diagnosis.DiagnosisAndAnalysisScreen
import com.leishmaniapp.presentation.ui.views.diagnosis.DiagnosisImageEditScreen
import com.leishmaniapp.presentation.ui.views.diagnosis.DiagnosisImageGridScreen
import com.leishmaniapp.presentation.ui.views.diagnosis.DiagnosisTableScreen
import com.leishmaniapp.presentation.ui.views.diagnosis.FinalizeDiagnosisScreen
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
            val diagnosis by diagnosisViewModel.diagnosis.collectAsStateWithLifecycle()

            // Trigger this code only when patient value actually changes
            LaunchedEffect(key1 = patient, key2 = specialist, key3 = diagnosis) {

                // Get finalized diagnosis values
                if (diagnosis != null && diagnosis!!.finalized) {
                    navHostController.navigateToDiagnosisTable()
                    return@LaunchedEffect
                }
                // Recover diagnosis
                else if (diagnosis != null) {
                    navHostController.navigateToPictureTake()
                    return@LaunchedEffect
                }

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

            BackHandler {
                willPopScope = true
            }

            CameraPermissionHandler {
                CameraScreen(executor = ContextCompat.getMainExecutor(context),
                    lifecycleOwner = lifecycleOwner,
                    cameraCalibration = cameraViewModel.cameraCalibration,
                    onPictureTake = {
                        cameraViewModel.onPictureTake(context, it, disease!!)
                    },
                    onPictureInjection = { uri ->
                        if (BuildConfig.DEBUG && uri != null) {
                            cameraViewModel.onPictureInjectDebug(context, uri, disease!!)
                        }
                    },
                    onCancel = {
                        willPopScope = true
                    })
            }

            when {
                // Show busy camera
                state is CameraState.Busy -> BusyAlertDialog()

                // Show an error alert dialog
                state is CameraState.Error -> ErrorAlertDialog(error = (state as CameraState.Error).e) {
                    cameraViewModel.dismiss()
                }

                // Navigate to next page
                state is CameraState.Photo -> LaunchedEffect(state) {
                    navHostController.navigateToDiagnosisAndAnalysis()
                }

                // Show exit alert dialog
                willPopScope -> WillPopCameraAlertDialog(
                    onDismissRequest = {
                        willPopScope = false
                    },
                    onConfirmExit = {
                        // Navigate to patient selection or go to main menu
                        if (navHostController.previousBackStackEntry?.destination?.route == NavigationRoutes.DiagnosisRoute.InitializeDiagnosis.route) {
                            navHostController.navigate(NavigationRoutes.MenuRoute.MainMenuRoute.route) {
                                popUpTo(NavigationRoutes.MenuRoute.MainMenuRoute.route)

                                // ! Cancel the diagnosis
                                // Dismiss the current camera state
                                cameraViewModel.dismiss()
                                // Dismiss the current patient
                                patientViewModel.dismiss()
                                // Discard the diagnosis
                                diagnosisViewModel.discard()
                            }
                        } else {
                            // Recover the latest camera state
                            cameraViewModel.recover()
                            navHostController.popBackStack()
                        }
                    },
                )
            }

        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisAndAnalysis.route) {

            // Get the context
            val context = LocalContext.current

            // Get the camera and diagnosis state
            val cameraState by cameraViewModel.cameraState.observeAsState()
            val currentDiagnosis by diagnosisViewModel.diagnosis.collectAsStateWithLifecycle()
            val currentImage by diagnosisViewModel.currentImageSample.collectAsStateWithLifecycle()

            // Dialog state
            var showSureFinishAlertDialog by rememberSaveable { mutableStateOf(false) }

            when (cameraState) {
                // Return to picture take
                is CameraState.Error, CameraState.None -> LaunchedEffect(cameraState) {
                    navHostController.navigateToPictureTake {
                        popUpTo(NavigationRoutes.DiagnosisRoute.DiagnosisAndAnalysis.route)
                    }
                }

                // Show a loading screen
                null, CameraState.Busy -> BusyScreen()

                // Show the analysis screen
                is CameraState.Photo -> {

                    LaunchedEffect(key1 = cameraState) {

                        (cameraState as CameraState.Photo).let { st ->
                            if (st.recovery) {
                                // Recover latest image
                                diagnosisViewModel.setPreviousImageSample()
                            } else {
                                // Set the image sample
                                diagnosisViewModel.setNewImageSample(st.location)
                            }
                        }
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
                                analysisInProgress = (currentImage!!.stage == AnalysisStage.Analyzing),
                                onImageChange = { image ->
                                    diagnosisViewModel.updateImageSample(image)
                                },
                                onRepeatAction = {
                                    diagnosisViewModel.discardCurrentImageSample()
                                    cameraViewModel.dismiss()
                                },
                                onAnalyzeAction = {
                                    diagnosisViewModel.startSampleAnalysis()
                                },
                                onNextAction = {
                                    diagnosisViewModel.saveImageSample(context)
                                    cameraViewModel.dismiss()
                                },
                                onFinishAction = {
                                    showSureFinishAlertDialog = true
                                },
                            )
                        }
                    }
                }
            }

            if (showSureFinishAlertDialog) {
                FinalizeDiagnosisAlertDialog(onDismissRequest = {
                    showSureFinishAlertDialog = false
                }, onConfirmFinalize = {
                    diagnosisViewModel.saveImageSample(context)
                    navHostController.navigateToDiagnosisImageGrid()
                    showSureFinishAlertDialog = false
                })
            }
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisImageGrid.route) {

            // Get the current diagnosis
            val diagnosis by diagnosisViewModel.diagnosis.collectAsStateWithLifecycle()

            // Dialog state
            var showSureFinishAlertDialog by remember { mutableStateOf(false) }
            var showSureBackgroundAlertDialog by remember { mutableStateOf(false) }

            // Show loading screen
            if (diagnosis == null) {
                BusyScreen()
                return@composable
            }

            DiagnosisImageGridScreen(
                diagnosis = diagnosis!!,
                onBackgroundProcessing = {
                    showSureBackgroundAlertDialog = true
                },
                onGoBack = {
                    navHostController.popBackStack()
                },
                onFinalizeDiagnosis = {
                    showSureFinishAlertDialog = true
                },
                onImageClick = { image ->
                    diagnosisViewModel.setCurrentImageSample(image)
                    navHostController.navigateToDiagnosticImageEdit()
                },
            )

            if (showSureBackgroundAlertDialog) {
                BackgroundDiagnosisAlertDialog(onDismissRequest = {
                    showSureBackgroundAlertDialog = false
                }, onConfirmBackground = {
                    // Set the new diagnosis values, dismiss and return
                    diagnosisViewModel.setBackgroundDiagnosis()
                    diagnosisViewModel.dismiss()
                    navHostController.navigateReturnToMenu()
                })
            }

            if (showSureFinishAlertDialog) {
                FinalizeDiagnosisAlertDialog(onDismissRequest = {
                    showSureFinishAlertDialog = false
                }, onConfirmFinalize = {
                    navHostController.navigateToDiagnosticRemarks()
                })
            }
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosticImageEdit.route) {

            val diagnosis by diagnosisViewModel.diagnosis.collectAsStateWithLifecycle()
            val image by diagnosisViewModel.currentImageSample.collectAsStateWithLifecycle()

            // Show loading screen
            if (diagnosis == null || image == null) {
                BusyScreen()
                return@composable
            }

            DiagnosisImageEditScreen(
                diagnosis = diagnosis!!,
                image = image!!,
                onImageChange = { i ->
                    diagnosisViewModel.updateImageSample(i)
                },
                onExit = {
                    navHostController.popBackStack()
                },
                onReanalyze = {
                    diagnosisViewModel.startSampleAnalysis()
                }
            )
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosticRemarks.route) {

            val diagnosis by diagnosisViewModel.diagnosis.collectAsStateWithLifecycle()

            if (diagnosis == null) {
                BusyScreen()
                return@composable
            }

            if (diagnosis!!.finalized) {
                BusyAlertDialog()
                LaunchedEffect(diagnosis!!.finalized) {
                    navHostController.navigateToDiagnosisTable()
                }
            }

            FinalizeDiagnosisScreen(
                diagnosis = diagnosis!!,
                onGoBack = { navHostController.popBackStack() },
                onDiagnosisFinish = { remarks, result ->
                    diagnosisViewModel.finalizeDiagnosis(remarks, result)
                }
            )
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisTable.route) {

            val context = LocalContext.current
            val diagnosis by diagnosisViewModel.diagnosis.collectAsStateWithLifecycle()

            if (diagnosis == null) {
                BusyScreen()
                return@composable
            }

            BackHandler {
                diagnosisViewModel.dismiss()
                navHostController.navigateReturnToMenu()
            }

            DiagnosisTableScreen(
                diagnosis = diagnosis!!,
                onBackButton = {
                    diagnosisViewModel.dismiss()
                    navHostController.navigateReturnToMenu()
                },
                onShareDiagnosis = {
                    diagnosisViewModel.shareDiagnosis(context)
                }
            )
        }

        composable(NavigationRoutes.DiagnosisRoute.AwaitingDiagnosis.route) {

            val specialist by sessionViewModel.specialist.collectAsStateWithLifecycle()
            val awaitingDiagnoses by diagnosisViewModel.awaitingDiagnoses.collectAsStateWithLifecycle()

            if (specialist == null || awaitingDiagnoses == null) {
                BusyScreen()
                return@composable
            }

            AwaitingDiagnosesScreen(
                specialist = specialist!!,
                awaitingDiagnoses = awaitingDiagnoses!!,
                onBackButton = { navHostController.popBackStack() },
                onDiagnosisClick = { diagnosis ->
                    diagnosisViewModel.setCurrentDiagnosis(diagnosis)
                    navHostController.navigateToDiagnosisImageGrid()
                },
            )

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

/**
 * Navigate to the results table for the diagnosis
 */
internal fun NavHostController.navigateToDiagnosisTable(
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(NavigationRoutes.DiagnosisRoute.DiagnosisTable.route, builder)
}

/*
 * Navigate to the deferred diagnoses
 */
internal fun NavHostController.navigateToAwaitingDiagnoses(
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(NavigationRoutes.DiagnosisRoute.AwaitingDiagnosis.route, builder)
}

private fun NavHostController.navigateToPictureTake(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(NavigationRoutes.DiagnosisRoute.DiagnosisCamera.route, builder)
}

private fun NavHostController.navigateToDiagnosisAndAnalysis() {
    navigate(NavigationRoutes.DiagnosisRoute.DiagnosisAndAnalysis.route)
}

private fun NavHostController.navigateToDiagnosisImageGrid(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(NavigationRoutes.DiagnosisRoute.DiagnosisImageGrid.route, builder)
}

private fun NavHostController.navigateToDiagnosticImageEdit(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(NavigationRoutes.DiagnosisRoute.DiagnosticImageEdit.route, builder)
}

private fun NavHostController.navigateToDiagnosticRemarks(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(NavigationRoutes.DiagnosisRoute.DiagnosticRemarks.route, builder)
}
