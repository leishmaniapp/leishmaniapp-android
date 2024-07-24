package com.leishmaniapp.infrastructure.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.hasKeyWithValueOfType
import com.leishmaniapp.domain.protobuf.toProto
import com.leishmaniapp.domain.repository.IDiagnosesRepository
import com.leishmaniapp.domain.services.IAnalysisService
import com.leishmaniapp.domain.services.IDiagnosisUploadService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.UUID

@HiltWorker
class DiagnosisUploadWorker @AssistedInject constructor(

    @Assisted context: Context,
    @Assisted val workerParameters: WorkerParameters,

    // Services
    private val diagnosisUploadService: IDiagnosisUploadService,

    // Repositories
    private val diagnosesRepository: IDiagnosesRepository

) : CoroutineWorker(context, workerParameters) {

    companion object {

        /**
         * Tag for using with [Log]
         */
        val TAG: String = DiagnosisUploadWorker::class.simpleName!!

        const val DIAGNOSIS_KEY: String = "diagnosis_uuid"
    }

    override suspend fun doWork(): Result {

        // Check if worker has the required parameters
        if (!workerParameters.inputData.run {
                hasKeyWithValueOfType<String>(DIAGNOSIS_KEY)
            }) {
            Log.e(
                RemoteAnalysisQueuingWorker.TAG,
                "Invalid request parameters: ${workerParameters.inputData}"
            )
            return Result.failure()
        }

        // Get the diagnosis
        val diagnosis = withContext(Dispatchers.IO) {
            diagnosesRepository.getDiagnosis(
                UUID.fromString(workerParameters.inputData.getString(DIAGNOSIS_KEY)!!),
            ).first()
        }

        // Could not find the diagnosis
        if (diagnosis == null) {
            Log.e(RemoteAnalysisQueuingWorker.TAG, "Requested Diagnosis does not exist")
            return Result.failure()
        }

        // Upload the diagnosis
        diagnosisUploadService.upload(diagnosis.toProto()).fold(
            onSuccess = {
                Log.i(TAG, "Successfully uploaded diagnosis (${it.code})")
                return Result.success()
            },
            onFailure = {
                Log.e(TAG, "Failed to upload diagnosis", it)
                return Result.retry()
            }
        )
    }
}