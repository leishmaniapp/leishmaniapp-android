package com.leishmaniapp.infrastructure.picture

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.services.IPictureStandardization
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
    override fun cropPicture(pictureUri: Uri): Result<Unit> {
        try {
            // Check if picture URI is valid
            if (pictureUri.path == null) return Result.failure(NullPointerException())
            // Create the source bitmap
            val sourceBitmap = BitmapFactory.decodeFile(pictureUri.path)
            // Calculate image center
            val centerOffset = Offset(
                (sourceBitmap.width / 2).toFloat(), (sourceBitmap.height / 2).toFloat()
            )
            // Calculate image radius
            val imageRadius = ( // In terms of picture radius
                    (sourceBitmap.width / 2) * sqrt(1.0 / 2.0)).toFloat()
            // Cropping Area
            val cropRect = Rect(centerOffset, imageRadius)
            // Bitmap cropped
            val croppedBitmap = Bitmap.createBitmap(
                sourceBitmap,
                cropRect.left.toInt(),
                cropRect.top.toInt(),
                cropRect.width.toInt(),
                cropRect.height.toInt()
            )
            // Store new bitmap as jpg
            val newFile = File(pictureUri.path!!)
            val outputStream: FileOutputStream?
            // Try to write data
            try {
                // Open new file
                outputStream = FileOutputStream(newFile)
                // Store new file
                croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            } catch (e: IOException) {
                // Rethrow the exception
                throw e
            } finally {
                // Release resources
                croppedBitmap.recycle()
                sourceBitmap.recycle()
            }

        } catch (e: Throwable) {
            return Result.failure(e)
        }

        return Result.success(Unit)
    }

    /**
     * Scale picture to the [ImageSample.STD_IMAGE_RESOLUTION]
     */
    override fun scalePicture(pictureUri: Uri): Result<Int> {
        try {
            // Check if picture URI is valid
            if (pictureUri.path == null) return Result.failure(NullPointerException("Invalid Uri.path"))
            // Create the source bitmap
            val sourceBitmap = BitmapFactory.decodeFile(pictureUri.path) ?: return Result.failure(
                Exception("Invalid input file")
            )
            // Create the scaled image
            val scaledBitmap =
                Bitmap.createScaledBitmap(
                    sourceBitmap,
                    ImageSample.STD_IMAGE_RESOLUTION,
                    ImageSample.STD_IMAGE_RESOLUTION,
                    false
                ) // Sampling filtering (Smooths image)
            // Store new bitmap as jpg
            val newFile = File(pictureUri.path!!)
            val outputStream: FileOutputStream?
            // Try to write data
            try {
                // Open new file
                outputStream = FileOutputStream(newFile)
                // Store new file
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            } catch (e: IOException) {
                // Rethrow the exception
                throw e
            } finally {
                // Release resources
                scaledBitmap.recycle()
                sourceBitmap.recycle()
            }
        } catch (e: Throwable) {
            return Result.failure(e)
        }

        // Return the standarized image resolution
        return Result.success(ImageSample.STD_IMAGE_RESOLUTION)
    }
}