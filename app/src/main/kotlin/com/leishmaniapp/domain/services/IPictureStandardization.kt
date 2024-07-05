package com.leishmaniapp.domain.services

import android.graphics.Bitmap
import android.net.Uri
import com.leishmaniapp.domain.disease.Disease
import java.io.File

/**
 * Standardize a picture (crop and resize)
 */
interface IPictureStandardization {

    /**
     * Get the file extension
     */
    val fileExtension: String

    /**
     * Get the file mime type
     */
    val mimeType: String

    /**
     * Return a cropped copy of the [Bitmap] area to a standard position and aspect ratio (1:1)
     */
    fun crop(bitmap: Bitmap): Result<Bitmap>

    /**
     * Returned a resized copy of a [Bitmap] with a standarized size for the [Disease]
     */
    fun scale(bitmap: Bitmap, disease: Disease): Result<Bitmap>

    /**
     * Compress the [Bitmap] into a compressed format array
     */
    fun compress(bitmap: Bitmap): Result<ByteArray>

    /**
     * Store the [bitmap] into the provided [file]
     */
    fun store(file: File, bitmap: Bitmap): Result<Unit>
}