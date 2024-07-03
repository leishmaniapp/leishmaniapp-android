package com.leishmaniapp.domain.calibration

/**
 * Real time image data used for calibration
 */
data class ImageCalibrationData(

    /**
     * Containts the HSV computed values for every pixel
     * 1. Minimum
     * 2. Maximum
     * 3. Average
     */
    val hsv: Triple<HSV, HSV, HSV>,

    /**
     * Image luminance values
     */
    val luminance: Double,

    /**
     * Image contrast values
     */
    val contrast: Double,
) {
    companion object;

    /**
     * Color space Hue, Saturation, Value
     */
    data class HSV(
        var hue: Float,
        var saturation: Float,
        var value: Float,
    ) {
        companion object;
        override fun toString(): String = "HSV($hue, $saturation, $value)"
    }
}
