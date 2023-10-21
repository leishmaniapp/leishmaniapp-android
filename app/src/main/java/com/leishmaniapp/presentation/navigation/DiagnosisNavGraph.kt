package com.leishmaniapp.presentation.navigation

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.work.Operation
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.leishmaniapp.R
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ImageAnalysisStatus
import com.leishmaniapp.presentation.viewmodel.ApplicationViewModel
import com.leishmaniapp.presentation.viewmodel.DiagnosisViewModel
import com.leishmaniapp.presentation.views.diagnosis.CameraView
import com.leishmaniapp.presentation.views.diagnosis.DiagnosisAndAnalysisScreen
import com.leishmaniapp.presentation.views.diagnosis.DiagnosisTableScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


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

                diagnosisViewModel.currentImage.value = newImage
                diagnosisViewModel.storeImageInDatabase()

                navController.navigateToDiagnosisAndAnalysis()
            })
        }

        // Sample processing
        composable(NavigationRoutes.DiagnosisRoute.DiagnosisAndAnalysis.route) {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val diagnosis by diagnosisViewModel.currentDiagnosis.collectAsState()

            val imageFlow by diagnosisViewModel.imageFlow.collectAsState(initial = null)
            val analysisState = imageFlow?.processed ?: ImageAnalysisStatus.NotAnalyzed

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
                DiagnosisAndAnalysisScreen(
                    analysisStatus = analysisState,
                    diagnosis = diagnosis!!,
                    image = imageFlow!!.asApplicationEntity(),
                    analysisWasStarted = analysisWasStarted,
                    onImageChange = { editedImage ->
                        diagnosisViewModel.currentImage.value = editedImage
                    },
                    onAnalyzeAction = {
                        // Start the diagnosis
                        analysisWasStarted = true
                        // Get the worker
                        diagnosisViewModel.currentWorkerId.value =
                            diagnosisViewModel.analyzeImage(context)

                        coroutineScope.launch {
                            var coroutineWasResumed = false

                            try {
                                // Get worker information
                                val workerInfo = WorkManager.getInstance(context)
                                    .getWorkInfoByIdLiveData(diagnosisViewModel.currentWorkerId.value!!)
                                    .asFlow()

                                withTimeout(15_000) {
                                    suspendCoroutine { continuation ->
                                        // Collect state
                                        launch {
                                            workerInfo.collect { info ->
                                                when (info.state) {
                                                    // Resume execution
                                                    WorkInfo.State.RUNNING,
                                                    WorkInfo.State.SUCCEEDED,
                                                    WorkInfo.State.FAILED ->
                                                        if (!coroutineWasResumed) {
                                                            continuation.resume(Unit)
                                                            coroutineWasResumed = true
                                                        }
                                                    // Continue waiting
                                                    WorkInfo.State.BLOCKED,
                                                    WorkInfo.State.CANCELLED,
                                                    WorkInfo.State.ENQUEUED -> Unit
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (e: TimeoutCancellationException) {
                                // Check if coroutine was resumed
                                if (!coroutineWasResumed)
                                    diagnosisViewModel.setImageAsDeferred()
                            }
                        }
                    },
                    onFinishAction = {},
                    onNextAction = {},
                    onRepeatAction = {
                        // Cancel current request
                        WorkManager.getInstance(context)
                            .cancelWorkById(diagnosisViewModel.currentWorkerId.value!!)

                        // Go out
                        navController.navigateToRepeatPictureTake()
                    })


                Log.d(
                    "ImageProcessingState",
                    imageFlow!!.asApplicationEntity().processed.toString()
                )
            }
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


