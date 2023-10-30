package com.leishmaniapp.presentation.navigation

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.R
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ImageAnalysisStatus
import com.leishmaniapp.presentation.ui.LoadingScreen
import com.leishmaniapp.presentation.viewmodel.ApplicationViewModel
import com.leishmaniapp.presentation.viewmodel.DiagnosisViewModel
import com.leishmaniapp.presentation.views.diagnosis.CameraView
import com.leishmaniapp.presentation.views.diagnosis.DiagnosisAndAnalysisScreen
import com.leishmaniapp.presentation.views.diagnosis.DiagnosisImageEditScreen
import com.leishmaniapp.presentation.views.diagnosis.DiagnosisImageGridScreen
import com.leishmaniapp.presentation.views.diagnosis.DiagnosisTableScreen
import com.leishmaniapp.presentation.views.diagnosis.FinishDiagnosisScreen
import com.leishmaniapp.presentation.views.menu.AwaitingDiagnosesScreen
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@SuppressLint("RestrictedApi")
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

                // Create Image Entity
                val currentDiagnosis = diagnosisViewModel.currentDiagnosis.value!!
                val newImage = Image(
                    sample = currentDiagnosis.samples,
                    size = imageStandardizationResult!!,
                    path = uri
                )

                diagnosisViewModel.setCurrentImage(newImage)
                diagnosisViewModel.storeImageInDatabase()

                navController.navigateToDiagnosisAndAnalysis()
            })
        }

        // Sample processing
        composable(NavigationRoutes.DiagnosisRoute.DiagnosisAndAnalysis.route) {

            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val diagnosis by diagnosisViewModel.currentDiagnosis.collectAsState()

            val imageFlow = diagnosisViewModel.imageFlow

            var analysisWasStarted by remember {
                mutableStateOf(false)
            }

            if (imageFlow == null) {
                LoadingScreen()
            } else {
                val imageFlowState by imageFlow.collectAsState(initial = null)
                val analysisState = imageFlowState?.processed ?: ImageAnalysisStatus.NotAnalyzed

                if (imageFlowState == null) {
                    LoadingScreen()
                } else {
                    DiagnosisAndAnalysisScreen(analysisStatus = analysisState,
                        diagnosis = diagnosis!!,
                        image = imageFlowState!!.asApplicationEntity(),
                        analysisWasStarted = analysisWasStarted,
                        onImageChange = { editedImage ->
                            Log.d("ImageUpdate", "Updated image with content: $editedImage")
                            diagnosisViewModel.updateImage(editedImage)
                        },
                        onAnalyzeAction = {
                            // Start the diagnosis
                            analysisWasStarted = true
                            coroutineScope.launch {
                                diagnosisViewModel.analyzeImage(context)
                            }
                        },
                        onFinishAction = {
                            runBlocking {
                                diagnosisViewModel.finishDiagnosisPictureTaking(context)
                            }
                            navController.navigateToImageGrid()
                        },
                        onNextAction = {
                            Log.d("Diagnosis", "Continue to next image")
                            runBlocking {
                                diagnosisViewModel.continueDiagnosisNextImage(context)
                            }
                            navController.navigateToPictureTake()
                        },
                        onRepeatAction = {
                            diagnosisViewModel.discardAndRepeatCurrentImage(context)
                            navController.navigateToPictureTake()
                        })


                    Log.d(
                        "ImageProcessingState",
                        imageFlowState!!.asApplicationEntity().processed.toString()
                    )
                }
            }
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisTable.route) {
            val diagnosis by diagnosisViewModel.currentDiagnosis.collectAsState()
            DiagnosisTableScreen(diagnosis = diagnosis!!,
                onBackButton = { navController.navigateToMenu() },
                onShareDiagnosis = { diagnosisViewModel.shareCurrentDiagnosis() })
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisImageGrid.route) {

            val context = LocalContext.current
            val currentDiagnosis = diagnosisViewModel.currentDiagnosis.collectAsState()
            val diagnosis = diagnosisViewModel.diagnosisFlow!!.collectAsState(initial = null)
            val imagesForDiagnosis = diagnosisViewModel.imagesForDiagnosisFlow

            if (imagesForDiagnosis == null || diagnosis.value == null) {
                LoadingScreen()
            } else {
                val imagesForDiagnosisState by imagesForDiagnosis.collectAsState(initial = null)

                if (imagesForDiagnosisState == null) {
                    LoadingScreen()
                } else {
                    val images = imagesForDiagnosisState!!.map { it.asApplicationEntity() }
                        .associateBy { it.sample }
                    val diagnosisEntity = diagnosis.value!!.asApplicationEntity(
                        applicationViewModel.specialist!!,
                        currentDiagnosis.value!!.patient,
                        images.values.toList()
                    )
                    DiagnosisImageGridScreen(diagnosis = diagnosisEntity,
                        allowReturn = diagnosisViewModel.isNewDiagnosis &&
                                !diagnosisEntity.finalized,
                        isBackground = !diagnosisViewModel.isNewDiagnosis,
                        onBackgroundProcessing = {
                            diagnosisViewModel.sendDiagnosisToBackgroundProcessing(context)
                            navController.exitDiagnosisReturnToMenu()
                        },
                        onGoBack = {
                            if (!diagnosis.value!!.finalized && diagnosisViewModel.isNewDiagnosis) {
                                navController.navigateToPictureTake()
                            } else {
                                navController.popBackStack()
                            }
                        },
                        onFinishDiagnosis = {
                            runBlocking {
                                diagnosisViewModel.finalizeDiagnosis(context)
                                navController.navigateToRemarks()
                            }
                        },
                        onImageClick = { image ->
                            diagnosisViewModel.setCurrentImage(image)
                            navController.navigateToEditImage()
                        })
                }
            }
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosticImageEdit.route) {

            val diagnosis by diagnosisViewModel.currentDiagnosis.collectAsState()
            val image by diagnosisViewModel.currentImage.collectAsState()

            if (image == null || diagnosis == null) {
                LoadingScreen()
            } else {
                DiagnosisImageEditScreen(diagnosis = diagnosis!!,
                    image = image!!,
                    onImageChange = { editedImage ->
                        Log.d("ImageUpdate", "Updated image with content: $editedImage")
                        diagnosisViewModel.updateImage(editedImage)
                    },
                    onExit = {
                        diagnosisViewModel.setCurrentImage(null)
                        navController.popBackStack()
                    })
            }
        }

        composable(NavigationRoutes.DiagnosisRoute.AwaitingDiagnosis.route) {
            val context = LocalContext.current
            AwaitingDiagnosesScreen(specialist = applicationViewModel.specialist!!,
                awaitingDiagnoses = runBlocking {
                    diagnosisViewModel.getAwaitingDiagnosis(applicationViewModel.specialist!!)
                },
                onBackButton = {
                    navController.popBackStack()
                },
                onDiagnosisClick = { diagnosis ->
                    diagnosisViewModel.setCurrentDiagnosis(diagnosis)
                    navController.navigateToImageGrid()
                },
                onSync = {
                    diagnosisViewModel.startDiagnosisResultOneTimeWorker(context)
                    Toast.makeText(
                        context, R.string.alert_background_processing, Toast.LENGTH_LONG
                    ).show()
                })
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosticRemarks.route) {
            val diagnosis by diagnosisViewModel.currentDiagnosis.collectAsState()
            FinishDiagnosisScreen(diagnosis = diagnosis!!,
                onGoBack = { navController.popBackStack() },
                onDiagnosisFinish = { newDiagnosis ->
                    runBlocking {
                        diagnosisViewModel.updateDiagnosis(newDiagnosis)
                        navController.navigateToDiagnosisHistory()
                    }
                })
        }
    }
}

fun NavHostController.navigateToDiagnosisHistory() {
    this.navigate(NavigationRoutes.DiagnosisRoute.DiagnosisTable.route)
}

fun NavHostController.navigateToStartDiagnosis() {
    this.navigate(NavigationRoutes.DiagnosisRoute.route)
}

fun NavHostController.navigateToAwaitingDiagnosis() {
    this.navigate(NavigationRoutes.DiagnosisRoute.AwaitingDiagnosis.route)
}

private fun NavHostController.navigateToRemarks() {
    this.navigate(NavigationRoutes.DiagnosisRoute.DiagnosticRemarks.route)
}

private fun NavHostController.navigateToDiagnosisAndAnalysis() {
    this.navigate(NavigationRoutes.DiagnosisRoute.DiagnosisAndAnalysis.route) {
        popUpTo(NavigationRoutes.DiagnosisRoute.DiagnosisCamera.route) {
            inclusive = true
        }
    }
}

private fun NavHostController.navigateToPictureTake() {
    this.navigate(NavigationRoutes.DiagnosisRoute.DiagnosisCamera.route) {
        popUpTo(NavigationRoutes.DiagnosisRoute.DiagnosisAndAnalysis.route) {
            inclusive = true
        }
    }
}

private fun NavHostController.navigateToImageGrid() {
    this.navigate(NavigationRoutes.DiagnosisRoute.DiagnosisImageGrid.route) {
        popUpTo(NavigationRoutes.DiagnosisRoute.DiagnosisAndAnalysis.route) {
            inclusive = true
        }
    }
}

private fun NavHostController.navigateToEditImage() {
    this.navigate(NavigationRoutes.DiagnosisRoute.DiagnosticImageEdit.route)
}

private fun NavHostController.exitDiagnosisReturnToMenu() {
    this.navigate(NavigationRoutes.MenuRoute.MainMenuRoute.route) {
        popUpTo(NavigationRoutes.MenuRoute.MainMenuRoute.route) {
            inclusive = true
        }
    }
}