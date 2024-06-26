package com.leishmaniapp.presentation.ui.views.camera

import android.graphics.Bitmap
import android.os.SystemClock
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leishmaniapp.R
import com.leishmaniapp.infrastructure.camera.CameraCalibrationAnalyzer
import com.leishmaniapp.presentation.ui.composables.CameraCalibrationCard
import com.leishmaniapp.presentation.ui.dialogs.BusyAlertDialog
import com.leishmaniapp.presentation.ui.layout.CameraLayout
import java.util.concurrent.Executor

@Composable
fun CameraScreen(
    executor: Executor,
    cameraCalibration: CameraCalibrationAnalyzer,
    onCancel: () -> Unit,
    onPictureTake: (Bitmap) -> Unit,
    onError: (Exception) -> Unit,
) {

    // Application context and composable lifecycle
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Get the camera controller
    val cameraController = remember {
        LifecycleCameraController(context).apply {

            // Bind to the composble lifecycle
            bindToLifecycle(lifecycleOwner)

            // Set the image analyzer
            setImageAnalysisAnalyzer(executor, cameraCalibration)

            // Use back facing camera
            cameraSelector = CameraSelector
                .Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
        }
    }

    // Camera busy
    var cameraBusy by remember { mutableStateOf(false) }

    // Get camera state
    val imageProperties by cameraCalibration.properties.collectAsStateWithLifecycle(initialValue = null)

    // Get analysis time
    var analysisTime by remember { mutableLongStateOf(SystemClock.elapsedRealtime()) }
    var analysisDuration by remember { mutableLongStateOf(1L) }

    // Get the analysis time between properties change
    LaunchedEffect(key1 = imageProperties) {
        val currentTime = SystemClock.elapsedRealtime()
        analysisDuration = (currentTime - analysisTime)
        analysisTime = currentTime
    }

    CameraLayout(
        onCancel = onCancel,
        onPhotoTake = {
            // Set the camera as busy
            cameraBusy = true
            cameraController.takePicture(
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageCapturedCallback() {
                    // Send the picture to the callback
                    override fun onCaptureSuccess(image: ImageProxy) {
                        onPictureTake.invoke(image.toBitmap())
                        // Close the proxy
                        image.close()
                        // Set camera as not busy
                        cameraBusy = false
                    }

                    // Send the error callback
                    override fun onError(exception: ImageCaptureException) {
                        onError.invoke(exception)
                        // Set camera as not busy
                        cameraBusy = false
                    }
                })
        },
        calibration = {
            AnimatedContent(
                targetState = imageProperties,
                label = stringResource(id = R.string.calibration_title),
                contentKey = { imageProperties != null }
            ) { properties ->
                if (properties != null) {
                    CameraCalibrationCard(
                        modifier = Modifier.padding(16.dp),
                        calibrationData = properties,
                        leading = {
                            Text(
                                style = MaterialTheme.typography.labelMedium,
                                text = stringResource(
                                    id = R.string.calibration_properties_refresh,
                                    "mHz",
                                    (1_000_000.0 / analysisDuration.toFloat()),
                                ),
                            )
                        }
                    )
                }
            }

        },
    ) {
        CameraCanvasPreview(
            modifier = Modifier.weight(1f),
            cameraController = cameraController
        )
    }

    // Show loading screen
    if (cameraBusy) {
        BusyAlertDialog()
    }
}