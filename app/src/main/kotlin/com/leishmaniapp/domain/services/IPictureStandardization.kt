package com.leishmaniapp.domain.services

import android.net.Uri

/**
 * Standardize a picture (crop and resize)
 */
interface IPictureStandardization {
    /**
     * Crop the picture to standard position and aspect ratio
     */
    fun cropPicture(pictureUri: Uri): Result<Unit>

    /**
     * Resize the picture to standard size
     */
    fun scalePicture(pictureUri: Uri): Result<Int>
}