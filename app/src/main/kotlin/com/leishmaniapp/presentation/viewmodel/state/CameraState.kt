package com.leishmaniapp.presentation.viewmodel.state

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import com.leishmaniapp.domain.exceptions.LeishmaniappException
import kotlinx.parcelize.Parcelize

/**
 * Represents the camera state
 */
@Parcelize
sealed class CameraState : Parcelable {

    /**
     * Taking photo
     */
    @Parcelize
    data object None : CameraState()

    /**
     * Camera is busy processing the photo
     */
    @Parcelize
    data object Busy : CameraState()

    /**
     * Error during photo taking
     */
    @Parcelize
    data class Error(val e: LeishmaniappException) : CameraState()

    /**
     * A photo was taken, is recovery flag is true then the image was previously taken but recovered
     */
    @Parcelize
    data class Photo(val location: Uri, val recovery: Boolean = false) : CameraState()
}
