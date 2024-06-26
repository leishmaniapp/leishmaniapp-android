package com.leishmaniapp.infrastructure.camera

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.leishmaniapp.domain.calibration.ImageCalibrationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 * Analyze current camera frame and return a [ImageCalibrationData] object
 */
class CameraCalibrationAnalyzer(

    /**
     * Coroutine scope to run computations into
     */
    private val scope: CoroutineScope,

    ) : ImageAnalysis.Analyzer {

    private val _properties = MutableSharedFlow<ImageCalibrationData>()

    /**
     * Get the [ImageCalibrationData] in realtime
     */
    val properties: SharedFlow<ImageCalibrationData> = _properties

    /**
     * Calculate the [ImageCalibrationData] for each frame the camera preview is rendered.
     * IMPORTANT: This functions runs on EVERY FRAME (60 per second), if the operation takes
     * too much time to compute the user will experience lagging and thus the use of a [scope]
     * to run the computation asynchronously
     */
    override fun analyze(proxy: ImageProxy) {
        // Launch in a coroutine for async computations
        scope.launch(Dispatchers.Default) {
            // Autoclose the proxy once finished
            proxy.use { image ->
                // Transform into bitmap
                val bitmap = image.toBitmap()
                // Set the calibration properties
                _properties.emit(bitmap.computeProperties())
            }
        }
    }
}