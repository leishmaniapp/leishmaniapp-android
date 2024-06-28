package com.leishmaniapp.domain.services

import android.graphics.Bitmap
import android.net.Uri
import java.io.File

/**
 * Standardize a picture (crop and resize)
 */
interface IPictureStandardization {

    /**
     * Return a cropped copy of the [Bitmap] area to a standard position and aspect ratio
     */
    fun crop(bitmap: Bitmap): Result<Bitmap>

    /**
     * Returned a resized copy of a [Bitmap] with a standarized size
     */
    fun scale(bitmap: Bitmap): Result<Bitmap>

    /**
     * Compress the [Bitmap] into a compressed format array
     */
    fun compress(bitmap: Bitmap): Result<ByteArray>

    /**
     * Store the [bitmap] into the provided [file]
     */
    fun store(file: File, bitmap: Bitmap): Result<Unit>

    /**
     * Get the file extension
     */
    abstract val fileExtension: String
}