package com.leishmaniapp.presentation.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.leishmaniapp.R
import com.leishmaniapp.domain.disease.MockSpotsDisease
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.entities.ModelDiagnosticElement
import com.leishmaniapp.domain.types.BoxCoordinates
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utilities.mock.MockGenerator.mock

/// Radius at which the element will be
const val COORDINATES_SELECTION_RADIUS: Int = 100
const val OUTER_CIRCLE_RADIUS: Float = 0.20f
const val COLOR_ALPHA: Float = 0.20f

/**
 * Image's real coordinates do not match on-screen coordinates due to canvas being
 * smaller depending on the size of the devices thus coordinates need to be transformed
 */
fun transformCoordinatesFromRealToCanvas(
    realBoxCoordinates: BoxCoordinates, xreal: Int, yreal: Int, canvasSize: Size
): BoxCoordinates = BoxCoordinates(
    x = (realBoxCoordinates.x * (canvasSize.width / xreal)).toInt(),
    y = (realBoxCoordinates.y * (canvasSize.height / yreal)).toInt()
)

/**
 * Canvas coordinates like taps need to be transformed to image's real size
 */
fun transformCoordinatesFromCanvasToReal(
    canvasBoxCoordinates: BoxCoordinates, xreal: Int, yreal: Int, canvasSize: Size
): BoxCoordinates = BoxCoordinates(
    x = (canvasBoxCoordinates.x * (xreal / canvasSize.width)).toInt(),
    y = (canvasBoxCoordinates.y * (yreal / canvasSize.height)).toInt()
)

/**
 * Calculate the Painter's center of mass offset
 */
fun calculatePainterCenterOfMass(boxCoordinates: BoxCoordinates, imageSize: Size): BoxCoordinates =
    BoxCoordinates(
        x = boxCoordinates.x - (imageSize.width / 2f).toInt(),
        y = boxCoordinates.y - (imageSize.height / 2f).toInt()
    )

/**
 * Fetch user taps on Canvas
 * @SuppressLint("UnrememberedMutableInteractionSource") is required so state is rebuild
 * on each recomposition, otherwise the image coordinates won't be refreshed when a
 * [ModelDiagnosticElement] is removed
 */
@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun Modifier.onCanvasClick(
    onCompleted: (boxCoordinates: BoxCoordinates) -> Unit
): Modifier = this.pointerInput(MutableInteractionSource()) {
    awaitEachGesture {
        val tap = awaitFirstDown().apply { if (pressed != previousPressed) consume() }
        waitForUpOrCancellation()?.apply { if (pressed != previousPressed) consume() }?.let {
            onCompleted.invoke(
                BoxCoordinates(
                    tap.position.x.toInt(), tap.position.y.toInt()
                )
            )
        }
    }
}

@Composable
fun DiagnosticImage(
    modifier: Modifier = Modifier,
    image: ImageSample,
    clickEnabled: Boolean = true,
    selectedElement: Pair<ModelDiagnosticElement, BoxCoordinates>? = null,
    onElementPressed: (Pair<ModelDiagnosticElement, BoxCoordinates>?) -> Unit,
) {
    // Late initialization of canvas size when rendered
    var canvasSize: Size? = null

    // Get the Icon to be painted
    val iconPainter = rememberVectorPainter(Icons.Filled.Close)
    val iconPainterSize = 16.dp

    val elementsWithCoordinates =
        image.elements.filterIsInstance<ModelDiagnosticElement>().map { it to it.coordinates }
            .flatMap { (key, values) -> values.map { key to it } }


    if (image.file == null) {
        Image(
            modifier = modifier.aspectRatio(1f),
            painter = painterResource(id = R.drawable.disease_leishmaniasis_giemsa_icon),
            contentDescription = stringResource(id = R.string.diagnostic_image),
            contentScale = ContentScale.Crop,
        )
    } else {
        // Get the image bitmap
        val bitmap = image.bitmap!!

        // Create the image painter
        val imagePainter = rememberAsyncImagePainter(bitmap)

        Image(modifier = modifier
            .aspectRatio(1f)
            .drawWithContent {
                // Draw the image
                drawContent()

                // Guard (Do not draw if not processed
                if (image.stage != AnalysisStage.Analyzed) return@drawWithContent
                // Store the canvas size
                canvasSize = this.size
                // Get the Painter size in Px
                val painterSizePx = Size(iconPainterSize.toPx(), iconPainterSize.toPx())

                // For each coordinate
                elementsWithCoordinates.forEach { (element, coordinates) ->
                    // The coordinate must be included in the element coordinates
                    assert(coordinates in element.coordinates)

                    // Calculate canvas position with painter center of mass
                    var canvasPosition = transformCoordinatesFromRealToCanvas(
                        coordinates, bitmap.width, bitmap.height, canvasSize!!
                    )

                    // Center the element offset
                    canvasPosition = canvasPosition.copy(
                        x = canvasPosition.x,
                        y = canvasPosition.y,
                    )

                    // Draw the center of masses
                    if (coordinates.isCenterOfMass()) {
                        // Draw a circle behind the (x)
                        drawCircle(alpha = COLOR_ALPHA,
                            color = Color.Yellow,
                            // For some reason radius is multiplied by 2
                            radius = (COORDINATES_SELECTION_RADIUS.toFloat() / 2f),
                            center = canvasPosition.let {
                                Offset(x = it.x.toFloat(), y = it.y.toFloat())
                            })
                    } else {
                        // Draw a box
                        drawRect(
                            alpha = COLOR_ALPHA,
                            color = Color.Yellow,
                            topLeft = canvasPosition.let {
                                Offset(x = it.x.toFloat(), y = it.y.toFloat())
                            },
                            size = Size(
                                coordinates.w.toFloat(),
                                coordinates.h.toFloat(),
                            ),
                        )
                    }

                    // Draw an (x) on top of the DiagnosticElement
                    with(iconPainter) {
                        // Calculate offset due to center of mass
                        val painterPosition = calculatePainterCenterOfMass(
                            canvasPosition, painterSizePx
                        )
                        // Draw in canvas position
                        translate(
                            left = painterPosition.x.toFloat(),
                            top = painterPosition.y.toFloat(),
                        ) {
                            draw(
                                size = painterSizePx, colorFilter = ColorFilter.tint(
                                    if ((element to coordinates) == selectedElement) {
                                        Color.Magenta // Item is selected
                                    } else { // Item is not selected
                                        Color.Black
                                    }
                                )
                            )
                        }
                    }
                }
            }
            .onCanvasClick { tap ->
                if (clickEnabled) {
                    // Transform canvas coordinates to real coordinates
                    val tapToRealCoordinates = transformCoordinatesFromCanvasToReal(
                        tap, bitmap.width, bitmap.height, canvasSize!!
                    )

                    // Get the nearest element
                    val tappedElement = elementsWithCoordinates.minByOrNull { (_, coordinates) ->
                        coordinates distanceTo tapToRealCoordinates
                    }

                    // Invoke with callback with null or value
                    onElementPressed.invoke(tappedElement?.let { (element, coordinates) ->
                        if (coordinates distanceTo tapToRealCoordinates <= COORDINATES_SELECTION_RADIUS) {
                            element to coordinates
                        } else null
                    })
                }
            },
            painter = imagePainter,
            contentDescription = stringResource(id = R.string.diagnostic_image),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DiagnosticElementMarkPreview() {
    LeishmaniappTheme {
        // Generate a mock image
        val image = ImageSample.mock(stage = AnalysisStage.Analyzed).copy(
            elements = setOf(
                ModelDiagnosticElement(
                    MockSpotsDisease.elements.first(), "n/a", setOf(
                        BoxCoordinates(100, 100, 10, 10),
                        BoxCoordinates(200, 250, 10, 10),
                        BoxCoordinates(150, 1050, 10, 10),
                        BoxCoordinates(1000, 1500, 10, 10),
                        BoxCoordinates(2000, 1500, 10, 10),
                    )
                ), ModelDiagnosticElement(
                    MockSpotsDisease.elements.last(), "n/a", setOf(
                        BoxCoordinates(550, 600),
                        BoxCoordinates(2200, 2000),
                        BoxCoordinates(450, 1903),
                    )
                )
            )
        )

        // Initialize with no selected element
        var selectedElement by remember {
            mutableStateOf<Pair<ModelDiagnosticElement, BoxCoordinates>?>(null)
        }

        // Create the diagnostic image
        DiagnosticImage(
            image = image,
            selectedElement = selectedElement
        ) { tappedSelectedElement -> selectedElement = tappedSelectedElement }
    }
}