package com.leishmaniapp.infrastructure.camera

import com.leishmaniapp.domain.calibration.ImageCalibrationData
import com.leishmaniapp.utilities.types.Triplet

/**
 * Compute simple luminance values getting the average value on HSV
 */
internal fun Triplet<ImageCalibrationData.HSV>.computeSimpleLuminance(): Double =
    third.value.toDouble()

/**
 * Compute the *Michelson Contrast* operation on HSV values
 * (Imax - Imin) / (Imax + Imin)
 */
internal fun Triplet<ImageCalibrationData.HSV>.computeMichelsonContrast(): Double =
    (second.value.toDouble() - first.value.toDouble()) /
            (second.value.toDouble() + first.value.toDouble())