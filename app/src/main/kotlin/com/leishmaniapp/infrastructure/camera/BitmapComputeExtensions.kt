package com.leishmaniapp.infrastructure.camera

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.get
import com.leishmaniapp.domain.calibration.ImageCalibrationData
import com.leishmaniapp.utilities.types.Triplet
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Compute the [ImageCalibrationData] from a [Bitmap].
 * When adding new properties, make sure to run them in their own [async] block
 * and then call await() on the field assigment of [ImageCalibrationData]
 */
suspend fun Bitmap.computeProperties(): ImageCalibrationData =
    coroutineScope {
        // Calculate properties asynchronously
        val megapixels = async { computeTotalMegapixels() }
        val hsv = async { computeHSV() }

        // Wait for all the results
        ImageCalibrationData(
            megapixels = megapixels.await(),
            hsv = hsv.await(),
            luminance = hsv.await().computeSimpleLuminance(),
            contrast = hsv.await().computeMichelsonContrast()
        )
    }

/**
 * Calculate the [ImageCalibrationData.megapixels] field from a [Bitmap]
 */
internal fun Bitmap.computeTotalMegapixels(): Double =
    (width.toDouble() * height.toDouble()) / 1_000_000


/**
 * Return a [Triplet] in which the first value is the minimum of each,
 * the second value, is the maximum, and the third value is the average
 */
internal fun Bitmap.computeHSV(): Triplet<ImageCalibrationData.HSV> {

    // Array in which HSV values will be stored
    val hsv = FloatArray(3)

    // Create the hsv average
    val average = ImageCalibrationData.HSV(0.0f, 0.0f, 0.0f)

    // Create maximum and minimums
    val min = ImageCalibrationData.HSV(
        Float.POSITIVE_INFINITY,
        Float.POSITIVE_INFINITY,
        Float.POSITIVE_INFINITY
    )
    val max = ImageCalibrationData.HSV(
        Float.NEGATIVE_INFINITY,
        Float.NEGATIVE_INFINITY,
        Float.NEGATIVE_INFINITY
    )

    // Total image size
    val size: Float = width.toFloat() * height.toFloat()

    // Traverse all the pixels
    for (y in 0..<height) {
        for (x in 0..<width) {
            // Transform the pixel to HSV
            Color.colorToHSV(get(x, y), hsv)

            // Add values for average
            average.hue += hsv[0]
            average.saturation += hsv[1]
            average.value += hsv[2]

            when {
                // Calculate max values
                hsv[0] > max.hue -> max.hue = hsv[0]
                hsv[1] > max.saturation -> max.saturation = hsv[1]
                hsv[2] > max.value -> max.value = hsv[2]

                // Calculate minimum values
                hsv[0] < min.hue -> min.hue = hsv[0]
                hsv[1] < min.saturation -> min.saturation = hsv[1]
                hsv[2] < min.value -> min.value = hsv[2]
            }

        }
    }

    // Get the average
    average.apply {
        hue /= size
        saturation /= size
        value /= size
    }

    // Return the computed value
    return Triplet(
        min,
        max,
        average
    )
}