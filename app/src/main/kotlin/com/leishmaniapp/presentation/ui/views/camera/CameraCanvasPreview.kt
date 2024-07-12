package com.leishmaniapp.presentation.ui.views.camera

import android.view.Surface
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
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
import androidx.compose.ui.viewinterop.AndroidView
import kotlin.math.sqrt

@Composable
fun CameraCanvasPreview(
    modifier: Modifier = Modifier,
    cameraController: LifecycleCameraController
) {
    Box(
        modifier = modifier.clipToBounds(),
        contentAlignment = Alignment.Center,
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PreviewView(ctx).apply {
                    controller = cameraController
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    implementationMode = PreviewView.ImplementationMode.PERFORMANCE
                }
            },
        )

        // Draw the square overlay
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                Path().apply {
                    addOval(Rect(center, size.minDimension / 2))
                }.also { path ->

                    // Draw mask outside the objective
                    clipPath(path, clipOp = ClipOp.Difference) {
                        drawRect(SolidColor(Color.Black.copy(alpha = 0.8f)))
                    }

                    // Draw the input shape rectangle
                    clipPath(path) {

                        val squareSize = size.minDimension / 2 * sqrt(2f)
                        val squareLeft = center.x - squareSize / 2
                        val squareTop = center.y - squareSize / 2
                        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                        drawRect(
                            color = Color.White,
                            topLeft = Offset(squareLeft, squareTop),
                            size = Size(squareSize, squareSize),
                            style = Stroke(width = 2.5f, pathEffect = dashEffect)
                        )
                    }
                }

            })
    }
}