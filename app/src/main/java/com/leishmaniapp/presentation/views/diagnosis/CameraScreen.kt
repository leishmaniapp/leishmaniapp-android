package com.leishmaniapp.presentation.views.diagnosis

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.leishmaniapp.R
import java.io.File
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.sqrt


@Composable
fun CameraScreen(
    outputFile: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit,
    onCancel: () -> Unit,
) {
    // Camera properties
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    /// Camera preview
    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()


    // Camera launch effect
    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }


    // Camera canvas with drawings
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        AndroidView(
            { previewView },
            modifier = Modifier.fillMaxWidth(),
        )

        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
            val circlePath = Path().apply {
                addOval(Rect(center, size.minDimension / 2))
            }
            clipPath(circlePath, clipOp = ClipOp.Difference) {
                drawRect(SolidColor(Color.Black.copy(alpha = 0.8f)))
            }
            clipPath(circlePath) {
                val squareSize = size.minDimension / 2 * sqrt(2f)
                val squareLeft = center.x - squareSize / 2
                val squareTop = center.y - squareSize / 2

                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                drawRect(
                    color = Color.White,
                    topLeft = Offset(squareLeft, squareTop),
                    size = Size(squareSize, squareSize),
                    style = Stroke(width = 5f, pathEffect = dashEffect)
                )
            }
        })

        // Cancel action button
        IconButton(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart),
            onClick = onCancel
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(id = R.string.cancel_camera),
                tint = Color.White,
                modifier = Modifier
                    .size(45.dp)
            )
        }


        // Take photo
        IconButton(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            onClick = {
                onCameraTakePhoto(
                    outputFile = outputFile,
                    imageCapture = imageCapture,
                    executor = executor,
                    onImageCaptured = onImageCaptured,
                    onError = onError
                )
            }) {
            Icon(
                imageVector = Icons.Sharp.Lens,
                contentDescription = "Take picture",
                tint = Color.White,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

private fun onCameraTakePhoto(
    outputFile: File,
    imageCapture: ImageCapture,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    // Create output options
    val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()

    imageCapture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
        override fun onError(exception: ImageCaptureException) {
            onError(exception)
            Log.d("ImageFile", outputFile.toString())
        }

        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            // Image capture callback with URI
            onImageCaptured(Uri.fromFile(outputFile))
            Log.d("ImageFile", outputFile.toString())
        }
    })
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }