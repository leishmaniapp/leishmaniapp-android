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
     * Error during photo taking
     */
    @Parcelize
    data class Error(val e: LeishmaniappException) : CameraState()

    /**
     * A photo was taken
     */
    @Parcelize
    data class Photo(val location: Uri) : CameraState()
}
