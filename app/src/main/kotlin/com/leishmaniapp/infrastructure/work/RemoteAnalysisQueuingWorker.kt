package com.leishmaniapp.infrastructure.work

import android.content.Context
import android.util.Log
import androidx.core.net.toFile
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.hasKeyWithValueOfType
import com.leishmaniapp.cloud.analysis.AnalysisRequest
import com.leishmaniapp.cloud.types.ImageBytes
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.domain.protobuf.toProto
import com.leishmaniapp.domain.repository.ISamplesRepository
import com.leishmaniapp.domain.repository.ISpecialistsRepository
import com.leishmaniapp.domain.services.IAnalysisService
import com.leishmaniapp.domain.services.IPictureStandardizationService
import com.leishmaniapp.utilities.extensions.toRecord
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okio.ByteString
import java.io.IOException
import java.util.UUID

/**
 * Send an [ImageSample] to a remote server for analysis.
 * Requires 4 input parameters:
 * [DIAGNOSIS_KEY]: [Diagnosis] UUID
 * [SPECIALIST_KEY]: [Specialist] email
 * [SAMPLE_KEY]: [ImageSample.metadata] sample number
 * [MIME_KEY]: Image MIME type from [IPictureStandardizationService]
 */
@HiltWorker
class RemoteAnalysisQueuingWorker @AssistedInject constructor(

    @Assisted context: Context,
    @Assisted val workerParameters: WorkerParameters,

    // Services
    private val analysisService: IAnalysisService,

    // Repositories
    private val specialistsRepository: ISpecialistsRepository,
    private val samplesRepository: ISamplesRepository,

    ) : CoroutineWorker(context, workerParameters) {

    companion object {

        /**
         * Tag for using with [Log]
         */
        val TAG: String = RemoteAnalysisQueuingWorker::class.simpleName!!

        const val DIAGNOSIS_KEY = "diagnosis_uuid"
        const val SPECIALIST_KEY = "specialist_email"
        const val SAMPLE_KEY = "sample_id"
        const val MIME_KEY = "mime_type"
    }

    override suspend fun doWork(): Result {

        // Check if worker has the required parameters
        if (!workerParameters.inputData.run {
                hasKeyWithValueOfType<String>(DIAGNOSIS_KEY) &&
                        hasKeyWithValueOfType<String>(SPECIALIST_KEY) &&
                        hasKeyWithValueOfType<Int>(SAMPLE_KEY) &&
                        hasKeyWithValueOfType<String>(MIME_KEY)
            }) {

            Log.e(TAG, "Invalid request parameters: ${workerParameters.inputData}")
            return Result.failure()
        }

        // Get the specialist
        val specialist = withContext(Dispatchers.IO) {
            specialistsRepository.specialistByEmail(
                workerParameters.inputData.getString(SPECIALIST_KEY)!!,
            ).first()
        }

        // Could not find the specialist
        if (specialist == null) {
            Log.e(TAG, "Requested Specialist does not exist")
            return Result.failure()
        }

        // Get the image sample
        val sample = withContext(Dispatchers.IO) {
            samplesRepository.getSampleForDiagnosis(
                UUID.fromString(workerParameters.inputData.getString(DIAGNOSIS_KEY)!!),
                workerParameters.inputData.getInt(SAMPLE_KEY, 0)
            ).first()
        }

        // Queue the sample
        Log.i(TAG, "Queuing sample for analysis ($sample) for analysis")

        // Could not find the sample
        if (sample == null) {
            Log.e(TAG, "Requested ImageSample does not exist")
            return Result.failure()
        }

        // Check the current stage (not an error but show warning)
        if (sample.stage != AnalysisStage.Enqueued) {
            Log.w(
                TAG,
                "Provided ImageSample is in (${sample.stage}) stage, which is not a subtype of ENQUEUED stage"
            )
        }

        // Sample does not contain a file
        if (sample.file == null) {
            Log.e(TAG, "Provided ImageSample does not contain a valid file Uri")
            return Result.failure()
        }

        // Read bytes frmo image
        val imageBytes = try {
            sample.file.toFile().readBytes()
        } catch (e: IOException) {
            Log.e(TAG, "Failed to read file from provided ImageSample", e)
            return Result.failure()
        }

        // Set the analysis status
        return withContext(Dispatchers.IO) {
            try {
                analysisService.analyze(
                    AnalysisRequest(
                        metadata = sample.metadata.toProto(),
                        specialist = specialist.toProto().toRecord(),
                        image = ImageBytes(
                            mime = workerParameters.inputData.getString(MIME_KEY)!!,
                            data_ = ByteString.of(*imageBytes)
                        )
                    )
                ).fold(
                    onSuccess = {
                        // Set the image status
                        samplesRepository.upsertSample(sample.copy(stage = AnalysisStage.Analyzing))
                        return@fold Result.success()
                    },
                    onFailure = { e ->
                        // Set the exception
                        Log.e(TAG, "Failed to connect to remote server", e)

                        // Set the image status
                        samplesRepository.upsertSample(sample.copy(stage = AnalysisStage.DeliverError))
                        return@fold Result.failure()
                    }
                )
            } catch (e: Exception) {
                // Set the exception
                Log.e(TAG, "Failed to send the AnalysisRequest to a remote server", e)

                // Set the image status
                samplesRepository.upsertSample(sample.copy(stage = AnalysisStage.DeliverError))
                return@withContext Result.retry()
            }
        }
    }
}