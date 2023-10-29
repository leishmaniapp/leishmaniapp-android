package com.leishmaniapp.infrastructure

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.leishmaniapp.usecases.IPictureStandardization
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import kotlin.math.sqrt

class ApplicationPictureStandardization @Inject constructor() : IPictureStandardization {

    companion object {
        const val imageResolution = 1944
    }

    override fun cropPicture(pictureUri: Uri): Boolean {
        // Check if picture URI is valid
        if (pictureUri.path == null) return false

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

        try {
            // Open new file
            outputStream = FileOutputStream(newFile)
            // Store new file
            croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        } catch (e: IOException) {
            return false
        } finally {
            // Release resources
            croppedBitmap.recycle()
            sourceBitmap.recycle()
        }
        return true
    }

    override fun scalePicture(pictureUri: Uri): Int? {
        // Check if picture URI is valid
        if (pictureUri.path == null) return null

        // Create the source bitmap
        val sourceBitmap = BitmapFactory.decodeFile(pictureUri.path) ?: return null

        // Create the scaled image
        val scaledBitmap =
            Bitmap.createScaledBitmap(
                sourceBitmap,
                imageResolution,
                imageResolution,
                false
            ) // Sampling filtering (Smooths image)

        // Store new bitmap as jpg
        val newFile = File(pictureUri.path!!)
        val outputStream: FileOutputStream?

        try {
            // Open new file
            outputStream = FileOutputStream(newFile)
            // Store new file
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        } catch (e: IOException) {
            return null
        } finally {
            // Release resources
            scaledBitmap.recycle()
            sourceBitmap.recycle()
        }
        return imageResolution
    }
}