package com.leishmaniapp.infrastructure.background

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.persistance.entities.DiagnosisRoom
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ImageProcessingPayload.Companion.toProcessingPayload
import com.leishmaniapp.persistance.database.ApplicationDatabase
import com.leishmaniapp.usecases.IProcessingRequest
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.UUID

/**
 * @param workerParameters Requires [Diagnosis.id] as String (with key: "diagnosis")
 * and [Image.sample] as Integer (with key: "sample")
 */
@HiltWorker
class ImageProcessingWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted val workerParameters: WorkerParameters,
    @Assisted val processingRequest: IProcessingRequest,
    @Assisted val applicationDatabase: ApplicationDatabase
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {

        // Obtain the parameters
        val diagnosisIdString = workerParameters.inputData.getString("diagnosis")
        val imageSample = workerParameters.inputData.getInt("sample", -1)

        // Check if parameters were sent
        if (diagnosisIdString == null || imageSample == -1) {
            return Result.failure()
        }

        // Diagnosis to UUID
        val diagnosisId = UUID.fromString(diagnosisIdString)

        // Bring data from database
        val diagnosis: DiagnosisRoom? =
            applicationDatabase.diagnosisDao().diagnosisForId(diagnosisId)
        val image: Image? =
            applicationDatabase.imageDao().imageForDiagnosis(diagnosisId, imageSample)
                ?.asApplicationEntity()

        // Check if values exist in database
        if (diagnosis == null || image == null || image.path == null) {
            return Result.failure()
        }

        // Make request
        return try {
            Log.i(
                "ImageProcessingWorker",
                "New request arrived for sample `${imageSample}` in diagnosis `${diagnosisIdString}`"
            )

            // Upload image to bucket and get the reference
            val reference = processingRequest.uploadImageToBucket(diagnosis.id, image)
            Log.d("ImageProcessingWorker", "Image reference: $reference")

            // Upload image metadata
//            processingRequest.generatePayloadRequest(
//                payload = image.toProcessingPayload(
//                    diagnosisId, diagnosis.disease, reference
//                )
//            )

            Result.success()

        } catch (e: Exception) {
            Log.e("ImageProcessingWorker", "Error: $e")
            //TODO: Retry request instead of fail
            Result.failure()
        }
    }

}