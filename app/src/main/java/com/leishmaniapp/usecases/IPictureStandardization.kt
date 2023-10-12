package com.leishmaniapp.usecases

import android.net.Uri

interface IPictureStandardization {
    fun cropPicture(pictureUri: Uri): Boolean
    fun scalePicture(pictureUri: Uri): Int?
}