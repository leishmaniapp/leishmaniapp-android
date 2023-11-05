package com.leishmaniapp.presentation.navigation

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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

            var showAlert by remember {
                mutableStateOf(false)
            }

            if (showAlert) {
                AlertDialog(onDismissRequest = {
                    showAlert = false
                }, dismissButton = {
                    TextButton(onClick = { showAlert = false }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                }, confirmButton = {
                    TextButton(onClick = {
                        // Show toast
                        navController.navigateToMenu()

                        Toast.makeText(context, R.string.camera_exit, Toast.LENGTH_SHORT).show()
                        runBlocking {
                            diagnosisViewModel.discardDiagnosis(context)
                        }

                    }) {
                        Text(text = stringResource(id = R.string.accept))
                    }
                }, text = { Text(text = stringResource(id = R.string.alert_discard_diagnosis)) })
            }

            BackHandler {
                showAlert = true
            }

            if (diagnosis == null) {
                LoadingScreen()
            } else {
                CameraView(diagnosis = diagnosis!!, onCanceled = {
                    // Return to previous
                    showAlert = true
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

                var alertDialogState by remember {
                    mutableStateOf("none")
                }

                val imageFlowState by imageFlow.collectAsState(initial = null)
                val analysisState = imageFlowState?.processed ?: ImageAnalysisStatus.NotAnalyzed

                if (imageFlowState == null) {
                    LoadingScreen()
                } else {
                    when (alertDialogState) {

                        "pop_scope" -> AlertDialog(onDismissRequest = {
                            alertDialogState = "none"
                        },
                            dismissButton = {
                                TextButton(onClick = { alertDialogState = "none" }) {
                                    Text(text = stringResource(id = R.string.cancel))
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    navController.popBackStack()
                                    runBlocking {
                                        diagnosisViewModel.discardDiagnosis(context)
                                    }
                                }) {
                                    Text(text = stringResource(id = R.string.accept))
                                }
                            },
                            text = { Text(text = stringResource(id = R.string.alert_discard_diagnosis)) })

                        "missing_specialist_result" -> AlertDialog(onDismissRequest = {
                            alertDialogState = "none"
                        }) {
                            Card {
                                Text(
                                    modifier = Modifier.padding(16.dp),
                                    text = stringResource(id = R.string.alert_missing_result)
                                )
                            }
                        }

                        "not_analyzed" -> AlertDialog(onDismissRequest = {
                            alertDialogState = "none"
                        }) {
                            Card {
                                Text(
                                    modifier = Modifier.padding(16.dp),
                                    text = stringResource(id = R.string.alert_not_analyzed)
                                )
                            }
                        }

                        "finish_sure" -> AlertDialog(onDismissRequest = {
                            alertDialogState = "none"
                        },
                            dismissButton = {
                                TextButton(onClick = { alertDialogState = "none" }) {
                                    Text(text = stringResource(id = R.string.cancel))
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    runBlocking {
                                        diagnosisViewModel.finishDiagnosisPictureTaking(context)
                                    }
                                    navController.navigateToImageGrid()
                                }) {
                                    Text(text = stringResource(id = R.string.accept))
                                }
                            },
                            text = { Text(text = stringResource(id = R.string.alert_sure_finish_diagnosis)) })
                    }

                    BackHandler {
                        alertDialogState = "pop_scope"
                    }

                    DiagnosisAndAnalysisScreen(analysisStatus = analysisState,
                        diagnosis = diagnosis!!,
                        image = imageFlowState!!.asApplicationEntity(),
                        analysisWasStarted = analysisWasStarted,
                        onImageChange = { editedImage ->
                            Log.d("ImageUpdate", "Updated image with content: $editedImage")
                            diagnosisViewModel.updateImage(editedImage)
                        },
                        onNextActionNotAnalyzed = {
                            alertDialogState = "not_analyzed"
                        },
                        onAnalyzeAction = {
                            // Start the diagnosis
                            analysisWasStarted = true
                            coroutineScope.launch {
                                diagnosisViewModel.analyzeImage(context)
                            }
                        },
                        onFinishAction = {
                            alertDialogState =
                                if (diagnosisViewModel.canContinueDiagnosisNextImage()) {
                                    "finish_sure"
                                } else {
                                    "missing_specialist_result"
                                }
                        },
                        onNextAction = {
                            Log.d("Diagnosis", "Continue to next image")
                            if (diagnosisViewModel.canContinueDiagnosisNextImage()) {
                                runBlocking {
                                    diagnosisViewModel.continueDiagnosisNextImage(context)
                                }
                                navController.navigateToPictureTake()
                            } else {
                                alertDialogState = "missing_specialist_result"
                            }
                        },
                        onRepeatAction = {
                            diagnosisViewModel.discardAndRepeatCurrentImage(context)
                            navController.navigateToPictureTake()
                        })
                }
            }
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisImageGrid.route) {

            val context = LocalContext.current
            val currentDiagnosis = diagnosisViewModel.currentDiagnosis.collectAsState()
            val imagesForDiagnosis = diagnosisViewModel.imagesForDiagnosisFlow

            var alertDialogState by remember {
                mutableStateOf("none")
            }

            if (imagesForDiagnosis == null || diagnosisViewModel.diagnosisFlow == null) {
                LoadingScreen()
            } else {
                val diagnosis = diagnosisViewModel.diagnosisFlow!!.collectAsState(initial = null)
                val imagesForDiagnosisState by imagesForDiagnosis.collectAsState(initial = null)

                if (imagesForDiagnosisState == null || diagnosis.value == null) {
                    LoadingScreen()
                } else {

                    val images = imagesForDiagnosisState!!.map { it.asApplicationEntity() }
                        .associateBy { it.sample }
                    val diagnosisEntity = diagnosis.value!!.asApplicationEntity(
                        applicationViewModel.specialist!!,
                        currentDiagnosis.value!!.patient,
                        images.values.toList()
                    )

                    when (alertDialogState) {

                        "pop_scope" -> AlertDialog(onDismissRequest = {
                            alertDialogState = "none"
                        },
                            dismissButton = {
                                TextButton(onClick = { alertDialogState = "none" }) {
                                    Text(text = stringResource(id = R.string.cancel))
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    navController.popBackStack()
                                    runBlocking {
                                        diagnosisViewModel.discardDiagnosis(context)
                                    }
                                }) {
                                    Text(text = stringResource(id = R.string.accept))
                                }
                            },
                            text = { Text(text = stringResource(id = R.string.alert_discard_diagnosis)) })

                        "finish_sure" -> AlertDialog(onDismissRequest = {
                            alertDialogState = "none"
                        },
                            dismissButton = {
                                TextButton(onClick = { alertDialogState = "none" }) {
                                    Text(text = stringResource(id = R.string.cancel))
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    runBlocking {
                                        navController.navigateToRemarks()
                                    }
                                }) {
                                    Text(text = stringResource(id = R.string.accept))
                                }
                            },
                            text = { Text(text = stringResource(id = R.string.alert_sure_finish_diagnosis)) })
                    }

                    if (diagnosisViewModel.isNewDiagnosis) {
                        BackHandler {
                            alertDialogState = "pop_scope"
                        }
                    }

                    DiagnosisImageGridScreen(diagnosis = diagnosisEntity,
                        allowReturn = diagnosisViewModel.isNewDiagnosis && !diagnosisEntity.finalized,
                        isBackground = !diagnosisViewModel.isNewDiagnosis,
                        onBackgroundProcessing = {
                            diagnosisViewModel.sendDiagnosisToBackgroundProcessing(context)
                            navController.exitDiagnosisReturnToMenu()
                        },
                        onGoBack = {
                            if (diagnosisViewModel.isNewDiagnosis) {
                                navController.navigateToPictureTake()
                            } else {
                                navController.popBackStack()
                            }
                        },
                        onFinishDiagnosis = {
                            alertDialogState = "finish_sure"
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

            val context = LocalContext.current
            val diagnosis by diagnosisViewModel.currentDiagnosis.collectAsState()

            FinishDiagnosisScreen(diagnosis = diagnosis!!,
                onGoBack = { navController.popBackStack() },
                onDiagnosisFinish = { newDiagnosis ->
                    runBlocking {
                        // Update the diagnosis with model results
                        diagnosisViewModel.updateDiagnosis(newDiagnosis.withModelResult())
                        diagnosisViewModel.finalizeDiagnosis(context)
                        navController.navigateToDiagnosisHistory()
                    }
                })
        }

        composable(NavigationRoutes.DiagnosisRoute.DiagnosisTable.route) {

            val diagnosis by diagnosisViewModel.currentDiagnosis.collectAsState()
            val context = LocalContext.current

            BackHandler {
                navController.navigateToMenu()
                diagnosisViewModel.restartState()
            }

            if (diagnosis == null) {
                LoadingScreen()
            } else {
                DiagnosisTableScreen(diagnosis = diagnosis!!,
                    onBackButton = {
                        navController.navigateToMenu()
                        diagnosisViewModel.restartState()
                    },
                    onShareDiagnosis = { diagnosisViewModel.shareCurrentDiagnosis(context) })
            }
        }
    }
}

fun NavHostController.navigateToDiagnosisHistory() {
    this.navigate(NavigationRoutes.DiagnosisRoute.DiagnosisTable.route) {
        popUpTo(NavigationRoutes.DiagnosisRoute.DiagnosisAndAnalysis.route) {
            inclusive = true
        }
    }
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