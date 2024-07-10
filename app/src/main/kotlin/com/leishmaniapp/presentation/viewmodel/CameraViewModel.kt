package com.leishmaniapp.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.exceptions.BadImageException
import com.leishmaniapp.domain.services.IPictureStandardization
import com.leishmaniapp.infrastructure.camera.CameraCalibrationAnalyzer
import com.leishmaniapp.presentation.viewmodel.state.CameraState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(

    /**
     * Keep state accross configuration changes
     */
    savedStateHandle: SavedStateHandle,

    // Services
    private val pictureStandardizationService: IPictureStandardization,

    ) : ViewModel(), DismissableState {

    companion object {
        /**
         * TAG to use with [Log]
         */
        val TAG: String = CameraViewModel::class.simpleName!!
    }

    /**
     * [CameraCalibrationAnalyzer] instance with coroutine lifecycle bound to the current view model
     * using the [viewModelScope]
     */
    val cameraCalibration = CameraCalibrationAnalyzer(viewModelScope)

    private val _cameraState: MutableLiveData<CameraState> =
        savedStateHandle.getLiveData("cameraState", CameraState.None)

    /**
     * Latest [CameraState]
     */
    val cameraState: LiveData<CameraState> = _cameraState

    /**
     * Callback when a picture is taken
     */
    fun onPictureTake(result: Result<Bitmap>, disease: Disease, context: Context) {
        File(
            context.filesDir,
            File.separator + "AnalysisLastTaken" + pictureStandardizationService.fileExtension
        ).let { file ->

            result.fold({ bitmap ->

                // Scale and Crop
                val newBitmap = pictureStandardizationService.scale(
                    pictureStandardizationService.crop(bitmap).getOrThrow(),
                    disease,
                ).getOrThrow()

                // Save the bitmap in storage
                pictureStandardizationService.store(file, newBitmap)

                // Set new bitmap value (from the URI)
                _cameraState.value = CameraState.Photo(file.toUri())
                Log.d(TAG, "Stored new photo with uri: ${_cameraState.value}")

                // Recycle the bitmap
                bitmap.recycle()

            }, { ex ->
                Log.e(TAG, "Failed to take picture", ex)
                // Set the error state
                _cameraState.value = CameraState.Error(BadImageException(ex))
            })
        }
    }

    /**
     * Dismiss all the current state
     */
    override fun dismiss() {
        // Set the new state
        _cameraState.value = CameraState.None
    }
}