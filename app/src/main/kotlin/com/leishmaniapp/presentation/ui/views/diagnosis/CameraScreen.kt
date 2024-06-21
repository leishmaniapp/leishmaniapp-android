//package com.leishmaniapp.presentation.ui.views.diagnosis
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.net.Uri
//import android.util.Log
//import android.view.ScaleGestureDetector
//import android.widget.Toast
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.ImageCaptureException
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.view.PreviewView
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.sharp.Lens
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableFloatStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Rect
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.ClipOp
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Path
//import androidx.compose.ui.graphics.PathEffect
//import androidx.compose.ui.graphics.SolidColor
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.graphics.drawscope.clipPath
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalLifecycleOwner
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.content.ContextCompat
//import com.leishmaniapp.R
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.io.File
//import java.util.concurrent.Executor
//import kotlin.coroutines.resume
//import kotlin.coroutines.suspendCoroutine
//import kotlin.math.sqrt
//
//
//@SuppressLint("ClickableViewAccessibility")
//@Composable
//fun CameraScreen(
//    outputFile: File,
//    executor: Executor,
//    onImageCaptured: (Uri) -> Unit,
//    onError: (ImageCaptureException) -> Unit,
//    onCancel: () -> Unit,
//) {
//    // Camera properties
//    val lensFacing = CameraSelector.LENS_FACING_BACK
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val coroutineScope = rememberCoroutineScope()
//
//    // Camera preview
//    val preview = Preview.Builder().build()
//    val previewView = remember { PreviewView(context) }
//    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
//
//    var zoomState by remember { mutableFloatStateOf(1f) } // Initial zoom ratio
//    val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
//
//    var isTakingPhoto by remember {
//        mutableStateOf(false)
//    }
//
//    // Camera launch effect
//    LaunchedEffect(lensFacing) {
//        val cameraProvider = context.getCameraProvider()
//        val camera = cameraProvider.bindToLifecycle(
//            lifecycleOwner, cameraSelector, preview, imageCapture
//        )
//
//        preview.setSurfaceProvider(previewView.surfaceProvider)
//
//        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
//            override fun onScale(detector: ScaleGestureDetector): Boolean {
//                zoomState *= detector.scaleFactor
//                zoomState =
//                    zoomState.coerceIn(1f, camera.cameraInfo.zoomState.value?.maxZoomRatio ?: 1f)
//                camera.cameraControl.setZoomRatio(zoomState)
//                return true
//            }
//        }
//
//        val scaleGestureDetector = ScaleGestureDetector(context, listener)
//
//        previewView.setOnTouchListener { _, event ->
//            scaleGestureDetector.onTouchEvent(event)
//            return@setOnTouchListener true
//        }
//    }
//
//
//    // Camera canvas with drawings
//    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
//        AndroidView(
//            { previewView },
//            modifier = Modifier.fillMaxWidth(),
//        )
//
//        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
//            val circlePath = Path().apply {
//                addOval(Rect(center, size.minDimension / 2))
//            }
//            clipPath(circlePath, clipOp = ClipOp.Difference) {
//                drawRect(SolidColor(Color.Black.copy(alpha = 0.8f)))
//            }
//            clipPath(circlePath) {
//                val squareSize = size.minDimension / 2 * sqrt(2f)
//                val squareLeft = center.x - squareSize / 2
//                val squareTop = center.y - squareSize / 2
//
//                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
//                drawRect(
//                    color = Color.White,
//                    topLeft = Offset(squareLeft, squareTop),
//                    size = Size(squareSize, squareSize),
//                    style = Stroke(width = 5f, pathEffect = dashEffect)
//                )
//            }
//        })
//
//        // Cancel action button
//        IconButton(
//            modifier = Modifier
//                .padding(16.dp)
//                .align(Alignment.TopStart), onClick = onCancel
//        ) {
//            Icon(
//                imageVector = Icons.Filled.Close,
//                contentDescription = stringResource(id = R.string.cancel_camera),
//                tint = Color.White,
//                modifier = Modifier.size(45.dp)
//            )
//        }
//
//
//        // Take photo
//        IconButton(
//            modifier = Modifier
//                .padding(16.dp)
//                .align(Alignment.BottomCenter), onClick = {
//                coroutineScope.launch {
//                    Toast.makeText(context, R.string.alert_taking_photo, Toast.LENGTH_SHORT).show()
//                    isTakingPhoto = true
//                    withContext(Dispatchers.IO) {
//                        onCameraTakePhoto(
//                            outputFile = outputFile,
//                            imageCapture = imageCapture,
//                            executor = executor,
//                            onImageCaptured = onImageCaptured,
//                            onError = onError
//                        )
//                    }
//                    isTakingPhoto = false
//                }
//            }, enabled = !isTakingPhoto
//        ) {
//            Icon(
//                imageVector = Icons.Sharp.Lens,
//                contentDescription = "Take picture",
//                tint = if (isTakingPhoto) {
//                    Color.Gray
//                } else {
//                    Color.White
//                },
//                modifier = Modifier.size(80.dp)
//            )
//        }
//    }
//}
//
//private suspend fun onCameraTakePhoto(
//    outputFile: File,
//    imageCapture: ImageCapture,
//    executor: Executor,
//    onImageCaptured: (Uri) -> Unit,
//    onError: (ImageCaptureException) -> Unit
//) {
//    // Create output options
//    val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()
//
//    suspendCoroutine { continuation ->
//        imageCapture.takePicture(outputOptions,
//            executor,
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onError(exception: ImageCaptureException) {
//                    onError(exception)
//                    continuation.resume(Unit)
//                    Log.d("ImageFile", outputFile.toString())
//                }
//
//                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                    // Image capture callback with URI
//                    onImageCaptured(Uri.fromFile(outputFile))
//                    continuation.resume(Unit)
//                    Log.d("ImageFile", outputFile.toString())
//                }
//            })
//    }
//}
//
//private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
//    suspendCoroutine { continuation ->
//        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
//            cameraProvider.addListener({
//                continuation.resume(cameraProvider.get())
//            }, ContextCompat.getMainExecutor(this))
//        }
//    }