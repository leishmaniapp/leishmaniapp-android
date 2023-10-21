package com.leishmaniapp.infrastructure.background

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.result.StorageUploadResult
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ImageProcessingPayload.Companion.toProcessingPayload
import com.leishmaniapp.persistance.database.ApplicationDatabase
import com.leishmaniapp.persistance.entities.ImageRoom
import com.leishmaniapp.usecases.IProcessingRequest
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.lang.Exception
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @param workerParameters Requires [Diagnosis.id] as String (with key: "diagnosis")
 * and [Image.sample] as Integer (with key: "sample")
 */
@HiltWorker
class ImageProcessingWorker @AssistedInject constructor(
    @ApplicationContext context: Context,
    @Assisted val workerParameters: WorkerParameters,
    @Assisted val processingRequest: IProcessingRequest,
    @Assisted val applicationDatabase: ApplicationDatabase
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        return try {
            Log.d("CustomWorker", "Initialized request")

            // Get parameters
            val imageSample = workerParameters.inputData.getInt("sample", -1)
            val diagnosisString = workerParameters.inputData.getString("diagnosis")

            // Check values
            if (imageSample == -1) Result.failure()
            if (diagnosisString == null) Result.failure()

            val diagnosisUuid = UUID.fromString(diagnosisString!!)

            Log.i("CustomWorker", "Requested for diagnosis $diagnosisUuid and sample $imageSample")
            val image =
                applicationDatabase.imageDao().imageForDiagnosis(diagnosisUuid, imageSample)!!
            val diagnosis = applicationDatabase.diagnosisDao().diagnosisForId(diagnosisUuid)!!

            val result =
                processingRequest.uploadImageToBucket(diagnosisUuid, image.asApplicationEntity())
            Log.d("CustomWorker", result)

            // Generate the payload
            val payload = image.asApplicationEntity()
                .toProcessingPayload(diagnosisUuid, diagnosis.disease, result)
            processingRequest.generatePayloadRequest(payload)
            Log.d("CustomWorker", "Updated payload")

            Result.success()

        } catch (e: Exception) {
            Log.e("CustomWorker", "Error: $e")
            Result.retry()
        }
    }
}