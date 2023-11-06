package com.leishmaniapp.infrastructure.background

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ImageAnalysisStatus
import com.leishmaniapp.entities.ImageProcessingPayload.Companion.toProcessingPayload
import com.leishmaniapp.persistance.database.ApplicationDatabase
import com.leishmaniapp.persistance.entities.ImageRoom
import com.leishmaniapp.usecases.IProcessingRequest
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID

/**
 * @param workerParameters Requires [Diagnosis.id] as String (with key: "diagnosis")
 * and [Image.sample] as Integer (with key: "sample")
 */
@HiltWorker
class ImageProcessingWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted val workerParameters: WorkerParameters,
    val processingRequest: IProcessingRequest,
    val applicationDatabase: ApplicationDatabase
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {

        var image: ImageRoom? = null

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

            image = applicationDatabase.imageDao().imageForDiagnosis(diagnosisUuid, imageSample)
            if (image == null) Result.failure()

            val diagnosis = applicationDatabase.diagnosisDao().diagnosisForId(diagnosisUuid)
            if (diagnosis == null) Result.failure()

            val (bucket, key) =
                processingRequest.uploadImageToBucket(diagnosisUuid, image!!.asApplicationEntity())
            Log.d("CustomWorker", "Bucket=$bucket, Key=$key")

            // Generate the payload
            val payload = image.asApplicationEntity()
                .toProcessingPayload(diagnosisUuid, diagnosis!!.disease, bucket, key)

            processingRequest.generatePayloadRequest(payload)
            Log.d("CustomWorker", "Updated payload")

            // Image uploaded correctly
            applicationDatabase.imageDao()
                .upsertImage(image.copy(processed = ImageAnalysisStatus.Analyzing))

            // Set the result as success
            Result.success()

        } catch (e: Exception) {
            Log.e("CustomWorker", "Error: $e")

            if (image != null) {
                applicationDatabase.imageDao()
                    .upsertImage(image.copy(processed = ImageAnalysisStatus.Deferred))
            }

            Result.retry()
        }
    }
}