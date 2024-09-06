package com.leishmaniapp.presentation.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.leishmaniapp.R
import com.leishmaniapp.domain.disease.MockSpotsDisease
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.entities.ModelDiagnosticElement
import com.leishmaniapp.domain.types.BoxCoordinates
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utilities.mock.MockGenerator.mock

val SELECTED_COLOR: Color = Color.Magenta
const val BORDER_STROKE_WIDTH: Float = 5f

const val CENTER_OF_MASS_DIAMETER: Int = 140

val CENTER_OF_MASS_FILL_COLOR: Color = Color.Yellow
const val CENTER_OF_MASS_FILL_ALPHA: Float = 0.10f

val CENTER_OF_MASS_STROKE_COLOR: Color = Color.Red
const val CENTER_OF_MASS_STROKE_ALPHA: Float = 0.50f

val BOUNDING_BOX_STROKE_COLOR: Color = Color.Blue
const val BOUNDING_BOX_STROKE_ALPHA: Float = 0.50f

val BOUNDING_BOX_FILL_COLOR: Color = Color.Cyan
const val BOUNDING_BOX_FILL_ALPHA: Float = 0.05f

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
 * Transfor a box size into canvas size
 */
fun transformSizeFromRealToCanvas(
    x: Size, realSize: Size, canvasSize: Size
): Size = Size(
    width = x.width * (canvasSize.width / realSize.width),
    height = x.height * (canvasSize.height / realSize.height),
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

                        canvasPosition
                            .let {
                                Offset(x = it.x.toFloat(), y = it.y.toFloat())
                            }
                            .let { center ->

                                // Draw a circle
                                drawCircle(
                                    alpha = CENTER_OF_MASS_FILL_ALPHA,
                                    color = CENTER_OF_MASS_FILL_COLOR,
                                    radius = (CENTER_OF_MASS_DIAMETER.toFloat() / 2f),
                                    center = center,
                                )

                                // Draw a border
                                drawCircle(
                                    alpha = CENTER_OF_MASS_STROKE_ALPHA,
                                    color = CENTER_OF_MASS_STROKE_COLOR,
                                    style = Stroke(width = BORDER_STROKE_WIDTH),
                                    radius = (CENTER_OF_MASS_DIAMETER.toFloat() / 2f),
                                    center = center,
                                    colorFilter = if ((element to coordinates) == selectedElement) {
                                        // Item is selected
                                        ColorFilter.tint(SELECTED_COLOR)
                                    } else {
                                        // Item is not selected
                                        null
                                    }
                                )
                            }

                    } else {
                        // Get properties
                        val size = transformSizeFromRealToCanvas(
                            Size(
                                coordinates.w.toFloat(),
                                coordinates.h.toFloat(),
                            ),
                            Size(
                                bitmap.width.toFloat(),
                                bitmap.height.toFloat(),
                            ),
                            canvasSize!!,
                        )

                        val position = canvasPosition.let {
                            Offset(x = it.x.toFloat(), y = it.y.toFloat())
                        }

                        // Draw a box
                        drawRect(
                            alpha = BOUNDING_BOX_FILL_ALPHA,
                            color = BOUNDING_BOX_FILL_COLOR,
                            style = Fill,
                            topLeft = position,
                            size = size,
                        )

                        // Draw the border
                        drawRect(
                            alpha = BOUNDING_BOX_STROKE_ALPHA,
                            color = BOUNDING_BOX_STROKE_COLOR,
                            style = Stroke(width = BORDER_STROKE_WIDTH),
                            topLeft = position,
                            size = size,
                            colorFilter = if ((element to coordinates) == selectedElement) {
                                // Item is selected
                                ColorFilter.tint(SELECTED_COLOR)
                            } else {
                                // Item is not selected
                                null
                            }
                        )

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
                        if (coordinates distanceTo tapToRealCoordinates <= CENTER_OF_MASS_DIAMETER) {
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