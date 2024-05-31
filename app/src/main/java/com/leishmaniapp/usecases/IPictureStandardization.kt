package com.leishmaniapp.usecases

import android.net.Uri

/**
 * Standardize a picture (crop and resize)
 */
interface IPictureStandardization {
    /**
     * Crop the picture to standard position and aspect ratio
     * TODO: Use typed exceptions
     */
    fun cropPicture(pictureUri: Uri): Boolean

    /**
     * Resize the picture to standard size
     * TODO: Use typed exceptions
     */
    fun scalePicture(pictureUri: Uri): Int?
}