package com.leishmaniapp.infrastructure.service.picture

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.services.IPictureStandardizationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.math.sqrt

/**
 * Transform picture into an standarized cloud format
 */
class ApplicationPictureStandardizationImpl @Inject constructor() : IPictureStandardizationService {

    override val fileExtension: String = ".jpeg"

    override val mimeType: String = "image/jpeg"

    /**
     * Crop the square inside the microscope objective lens circular area
     */
    override suspend fun crop(bitmap: Bitmap): Result<Bitmap> = try {

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
            withContext(Dispatchers.Default) {
                Bitmap.createBitmap(
                    bitmap,
                    cropRect.left.toInt(),
                    cropRect.top.toInt(),
                    cropRect.width.toInt(),
                    cropRect.height.toInt()
                )
            }
        )

    } catch (e: Throwable) {
        Result.failure(e)
    }

    /**
     * Scale picture to the [Disease.crop] size
     */
    override suspend fun scale(bitmap: Bitmap, disease: Disease): Result<Bitmap> =
        try {
            Result.success(
                withContext(Dispatchers.Default) {
                    Bitmap.createScaledBitmap(
                        bitmap,
                        disease.crop,
                        disease.crop,
                        false
                    )
                }
            )
        } catch (e: Throwable) {
            Result.failure(e)
        }

    /**
     * Compress the [Bitmap] into a JPEG [ByteArray]
     */
    override suspend fun compress(bitmap: Bitmap): Result<ByteArray> =
        try {
            Result.success(
                withContext(Dispatchers.IO) {
                    ByteArrayOutputStream().use { outputStream ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        outputStream.toByteArray()
                    }
                }
            )
        } catch (e: Throwable) {
            Result.failure(e)
        }

    /**
     * Store image file into
     */
    override suspend fun store(file: File, bitmap: Bitmap): Result<Unit> = try {
        withContext(Dispatchers.IO) {
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        }
        Result.success(Unit)
    } catch (e: Throwable) {
        Result.failure(e)
    }
}