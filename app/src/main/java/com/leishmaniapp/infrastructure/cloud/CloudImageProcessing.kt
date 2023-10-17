package com.leishmaniapp.infrastructure.cloud

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.WorkManager
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.Image
import com.leishmaniapp.persistance.entities.ImageRoom.Companion.asRoomEntity
import com.leishmaniapp.infrastructure.background.ImageProcessingWorker
import com.leishmaniapp.persistance.database.ApplicationDatabase
import com.leishmaniapp.usecases.IImageProcessing
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import javax.inject.Inject

class CloudImageProcessing @Inject constructor(
    @ApplicationContext val applicationContext: Context,
    val applicationDatabase: ApplicationDatabase,
) :
    IImageProcessing {
    override suspend fun processImage(
        diagnosis: Diagnosis,
        image: Image
    ): Operation {
        // Store image in database
        applicationDatabase.imageDao().upsertImage(image.asRoomEntity(diagnosis.id))

        // Build image parameters
        val inputData = Data.Builder()
            .putString("diagnosis", diagnosis.id.toString())
            .putInt("sample", image.sample)
            .build()

        // Call the worker
        return WorkManager.getInstance(applicationContext).enqueue(
            OneTimeWorkRequestBuilder<ImageProcessingWorker>().setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR, duration = Duration.ofSeconds(15)
            ).setInputData(inputData).build()
        )
    }
}