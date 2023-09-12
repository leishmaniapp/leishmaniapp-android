package com.leishmaniapp.presentation.ui

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.consumeDownChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    elementCoordinates: List<Pair<String, Coordinates>>,
    selectedElement: Pair<String, Coordinates>?,
    onElementPressed: (Pair<String, Coordinates>) -> Unit
) {
    /*TODO: Remove hardcoded image size, use image properties instead*/
    var canvasSize: Size? = null
    val pictureSize: Int = 2250
    val painter = rememberVectorPainter(Icons.Filled.Close)

    Image(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .drawWithContent {
                // Calculate the icon size
                val iconSize = Size(24.dp.toPx(), 24.dp.toPx())
                // Store the canvas size
                canvasSize = this.size

                drawContent()
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
        val diagnosticElements = MockGenerator.mockImage(true).diagnosticElements
            .filterIsInstance<ModelDiagnosticElement>().map { it.name to it.coordinates }
            .flatMap { (key, values) -> values.map { key to it } }

        val selectedElement: Pair<String, Coordinates>? = diagnosticElements.random()

        DiagnosticImage(
            elementCoordinates = diagnosticElements,
            selectedElement = selectedElement
        ) {

        }
    }
}