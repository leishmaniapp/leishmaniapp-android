package com.leishmaniapp.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
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
import com.leishmaniapp.R
import com.leishmaniapp.entities.Coordinates
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ModelDiagnosticElement
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

const val coordinatesOffset: Int = 50

fun calculateIconPositionPx(
    coordinates: Coordinates, iconSize: Size, canvasSize: Size, pictureSize: Int
): Pair<Float, Float> {
    val iconOffset = (iconSize.width / 2f) to (iconSize.height / 2f)
    return ((coordinates.x * canvasSize.width) / pictureSize) - (iconOffset.first) to ((coordinates.y * canvasSize.height) / pictureSize) - (iconOffset.second)
}

fun fromPositionToCoordinates(xOffset: Float, yOffset: Float, canvasSize: Size, pictureSize: Int) =
    Coordinates(
        x = ((xOffset * pictureSize) / canvasSize.width).toInt(),
        y = ((yOffset * pictureSize) / canvasSize.height).toInt()
    )

fun Coordinates.equalsWithinBoundary(other: Coordinates, coordinateOffset: Int): Boolean {
    return (((other.x >= x) && (x + coordinateOffset >= other.x)) ||
            ((other.x <= x) && (x - coordinateOffset <= other.x))) &&
            (((other.y >= y) && (y + coordinateOffset >= other.y)) ||
                    ((other.y <= y) && (y - coordinateOffset <= other.y)))
}

@Composable
fun Modifier.tapOrPress(
    onCompleted: (x: Float, y: Float) -> Unit
): Modifier {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    return this.pointerInput(interactionSource) {
        awaitEachGesture {
            val down = awaitFirstDown().also { if (it.pressed != it.previousPressed) it.consume() }
            val up = waitForUpOrCancellation()

            if (up != null) {
                if (up.pressed != up.previousPressed) up.consume()
                onCompleted(down.position.x, down.position.y)
            }
        }
    }
}

@Composable
fun DiagnosticImage(
    modifier: Modifier = Modifier,
    image: Image,
    selectedElement: Pair<String, Coordinates>? = null,
    onElementPressed: (Pair<String, Coordinates>) -> Unit
) {
    val elementCoordinates = image.diagnosticElements
        .filterIsInstance<ModelDiagnosticElement>().map { it.name to it.coordinates }
        .flatMap { (key, values) -> values.map { key to it } }

    /*TODO: Remove hardcoded image size, use image properties instead*/
    var canvasSize: Size? = null
    val pictureSize = 2250
    val painter = rememberVectorPainter(Icons.Filled.Close)

    Image(
        modifier = modifier
            .aspectRatio(1f)
            .drawWithContent {
                drawContent()

                // Guard
                if (!image.processed) {
                    return@drawWithContent;
                }

                // Calculate the icon size
                val iconSize = Size(24.dp.toPx(), 24.dp.toPx())
                // Store the canvas size
                canvasSize = this.size

                with(painter) {
                    elementCoordinates.forEach { element ->
                        val (xPosition, yPosition) = calculateIconPositionPx(
                            element.second,
                            iconSize,
                            this@drawWithContent.size,
                            pictureSize
                        )

                        translate(
                            left = xPosition,
                            top = yPosition,
                        ) {
                            draw(
                                size = iconSize, colorFilter =
                                if (element == selectedElement) {
                                    ColorFilter.tint(color = Color.Magenta)
                                } else {
                                    null
                                }
                            )
                        }
                    }
                }

            }
            .tapOrPress { x, y ->
                // Calculate the coordinates and the element found
                val coordinates = fromPositionToCoordinates(x, y, canvasSize!!, pictureSize)
                val foundElement =
                    elementCoordinates.filter {
                        coordinates.equalsWithinBoundary(
                            it.second,
                            coordinatesOffset
                        )
                    }

                // Invoke when pressed element
                if (foundElement.isNotEmpty()) {
                    onElementPressed.invoke(foundElement.first())
                }
            },
        painter = painterResource(id = R.drawable.image_example),
        contentDescription = stringResource(id = R.string.diagnostic_image),
        contentScale = ContentScale.Crop
    )
}

@Composable
@Preview
fun DiagnosticElementMarkPreview() {
    LeishmaniappTheme {

        val image = MockGenerator.mockImage(true)

        val diagnosticElements = image.diagnosticElements
            .filterIsInstance<ModelDiagnosticElement>()
            .map { it.name to it.coordinates }
            .flatMap { (key, values) -> values.map { key to it } }

        val selectedElement = diagnosticElements.random()

        DiagnosticImage(
            image = image,
            selectedElement = selectedElement
        ) {
        }
    }
}