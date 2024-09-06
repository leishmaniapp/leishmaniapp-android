package com.leishmaniapp.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.leishmaniapp.domain.services.IPictureStandardizationService
import com.leishmaniapp.infrastructure.camera.CameraCalibrationAnalyzer
import com.leishmaniapp.presentation.viewmodel.state.CameraState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(

    /**
     * Keep state accross configuration changes
     */
    savedStateHandle: SavedStateHandle,

    // Services
    private val pictureStandardizationService: IPictureStandardizationService,

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
     * URI of the latest image, used for recovering latest image
     */
    private var latestImagePath: Uri? = null

    /**
     * Callback when a picture is taken
     */
    fun onPictureTake(context: Context, result: Result<Bitmap>, disease: Disease) =
        viewModelScope.launch {

            withContext(Dispatchers.Main) {
                _cameraState.value = CameraState.Busy
            }

            File(
                context.cacheDir,
                // Short for LastTaken
                File.separator + "lt0" + pictureStandardizationService.fileExtension
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
                    latestImagePath = file.toUri()
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
     * Use an user-selected picture instead of a taken one (picture injection).
     * This is meant to be a DEBUG only feature
     */
    fun onPictureInjectDebug(context: Context, uri: Uri, disease: Disease) =
        viewModelScope.launch(Dispatchers.IO) {

            Log.w(TAG, "Request for image injection = $uri")
            withContext(Dispatchers.Main) {
                _cameraState.value = CameraState.Busy
            }

            // Create a file
            val file = File(
                context.cacheDir,
                File.separator + "lt1" + uri.path.let { p -> p!!.substring(p.lastIndexOf(".")) },
            )

            // Copy from origin to cacheFile
            context.contentResolver.openInputStream(uri)?.use { ins ->
                FileOutputStream(file).use { outs ->
                    ins.copyTo(outs)
                }
            }

            // Get bitmap from file
            val bitmap = pictureStandardizationService.scale(
                pictureStandardizationService.crop(BitmapFactory.decodeFile(file.path))
                    .getOrThrow(),
                disease,
            ).getOrThrow()

            // Call picture take with the new bitmap
            // Save the bitmap in storage
            pictureStandardizationService.store(file, bitmap)

            // Set new bitmap value (from the URI)
            Log.w(TAG, "Injected successfully: ${file.path}")

            withContext(Dispatchers.Main) {
                _cameraState.value = CameraState.Photo(file.toUri())
                latestImagePath = file.toUri()
            }

            bitmap.recycle()
        }

    /**
     * Recover latest taken photo
     */
    fun recover() {
        latestImagePath?.let { uri ->
            _cameraState.value = CameraState.Busy
            _cameraState.value = CameraState.Photo(uri, recovery = true)
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