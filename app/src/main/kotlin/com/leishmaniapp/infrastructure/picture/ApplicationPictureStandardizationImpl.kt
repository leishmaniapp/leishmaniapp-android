package com.leishmaniapp.infrastructure.picture

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.core.net.toFile
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.services.IPictureStandardization
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import kotlin.math.sqrt

/**
 * Transform picture into an standarized cloud format
 */
class ApplicationPictureStandardizationImpl @Inject constructor() : IPictureStandardization {

    /**
     * Crop the square inside the microscope objective lens circular area
     */
    override fun crop(bitmap: Bitmap): Result<Bitmap> = try {

        // Calculate image center
        val centerOffset = Offset(
            (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat()
        )

        // Calculate image radius
        val imageRadius = ( // In terms of picture radius
                (bitmap.width / 2) * sqrt(1.0 / 2.0)).toFloat()

        // Cropping Area
        val cropRect = Rect(centerOffset, imageRadius)

        // Return a new bitmap with the cropped area
        Result.success(
            Bitmap.createBitmap(
                bitmap,
                cropRect.left.toInt(),
                cropRect.top.toInt(),
                cropRect.width.toInt(),
                cropRect.height.toInt()
            )
        )

    } catch (e: Throwable) {
        Result.failure(e)
    }

    /**
     * Scale picture to the [ImageSample.STD_IMAGE_RESOLUTION]
     */
    override fun scale(bitmap: Bitmap): Result<Bitmap> =
        try {
            Result.success(
                Bitmap.createScaledBitmap(
                    bitmap,
                    ImageSample.STD_IMAGE_RESOLUTION,
                    ImageSample.STD_IMAGE_RESOLUTION,
                    false
                )
            )
        } catch (e: Throwable) {
            Result.failure(e)
        }

    /**
     * Compress the [Bitmap] into a JPEG [ByteArray]
     */
    override fun compress(bitmap: Bitmap): Result<ByteArray> =
        try {
            Result.success(
                ByteArrayOutputStream().use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.toByteArray()
                }
            )
        } catch (e: Throwable) {
            Result.failure(e)
        }

    /**
     * Store image file into
     */
    override fun store(file: File, bitmap: Bitmap): Result<Unit> = try {
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        Result.success(Unit)
    } catch (e: Throwable) {
        Result.failure(e)
    }

    /**
     * JPEG file extension
     */
    override val fileExtension: String = ".jpeg"
}