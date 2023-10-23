package com.leishmaniapp.presentation.navigation

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.R
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ImageAnalysisStatus
import com.leishmaniapp.presentation.viewmodel.ApplicationViewModel
import com.leishmaniapp.presentation.viewmodel.DiagnosisViewModel
import com.leishmaniapp.presentation.views.diagnosis.CameraView
import com.leishmaniapp.presentation.views.diagnosis.DiagnosisAndAnalysisScreen
import com.leishmaniapp.presentation.views.diagnosis.DiagnosisImageEditScreen
import com.leishmaniapp.presentation.views.diagnosis.DiagnosisImageGridScreen
import com.leishmaniapp.presentation.views.diagnosis.DiagnosisTableScreen
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

                //TODO: What if image fails?

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

            val imageFlow = diagnosisViewModel.imageFlow;

            var analysisWasStarted by remember {
                mutableStateOf(false)
            }

            if (imageFlow == null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val imageFlowState by imageFlow.collectAsState(initial = null)
                val analysisState = imageFlowState?.processed ?: ImageAnalysisStatus.NotAnalyzed

                if (imageFlowState == null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
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
                onBackButton = { navController.popBackStack() },
                onShareDiagnosis = { diagnosisViewModel.shareCurrentDiagnosis() })
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisImageGrid.route) {

            val diagnosis by diagnosisViewModel.currentDiagnosis.collectAsState()
            val imagesForDiagnosis = diagnosisViewModel.imagesForDiagnosisFlow

            if (imagesForDiagnosis == null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val imagesForDiagnosisState by imagesForDiagnosis.collectAsState(initial = null)

                if (imagesForDiagnosisState == null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    val images = imagesForDiagnosisState!!.map { it.asApplicationEntity() }
                        .associateBy { it.sample }

                    DiagnosisImageGridScreen(diagnosis = diagnosis!!.copy(images = images),
                        allowReturn = diagnosisViewModel.isNewDiagnosis && !diagnosis!!.finalized,
                        isBackground = !diagnosis!!.completed && !diagnosis!!.finalized,
                        onContinueDiagnosis = {
                            if (!diagnosis!!.finalized) {
                                navController.navigateToPictureTake()
                            }
                        },
                        onFinishDiagnosis = {
                            if (diagnosis!!.completed) {
                                // TODO: Finish diagnosis
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

            DiagnosisImageEditScreen(
                diagnosis = diagnosis!!,
                image = image!!,
                onImageChange = { editedImage ->
                    Log.d("ImageUpdate", "Updated image with content: $editedImage")
                    diagnosisViewModel.updateImage(editedImage)
                },
                onExit = {
                    navController.popBackStack()
                    diagnosisViewModel.setCurrentImage(null)
                })
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosticImageSection.route) {
// TODO
//            DiagnosticImageSection(image = image, onImageEdit = { /*TODO*/ }) {
//
//            }
        }

        composable(NavigationRoutes.DiagnosisRoute.AwaitingDiagnosis.route) {
            AwaitingDiagnosesScreen(
                specialist = applicationViewModel.specialist!!,
                awaitingDiagnoses = runBlocking {
                    diagnosisViewModel.getAwaitingDiagnosis(applicationViewModel.specialist!!)
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