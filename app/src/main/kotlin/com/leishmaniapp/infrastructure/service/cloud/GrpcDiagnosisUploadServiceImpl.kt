package com.leishmaniapp.infrastructure.service.cloud

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.leishmaniapp.cloud.diagnoses.DiagnosesServiceClient
import com.leishmaniapp.cloud.model.Diagnosis
import com.leishmaniapp.cloud.types.StatusResponse
import com.leishmaniapp.domain.services.IDiagnosisUploadService
import com.leishmaniapp.infrastructure.work.DiagnosisUploadWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.time.withTimeout
import kotlinx.coroutines.withContext
import java.time.Duration
import javax.inject.Inject

/**
 * [IDiagnosisUploadService] implementation for gRPC
 */
class GrpcDiagnosisUploadServiceImpl @Inject constructor(

    /**
     * [Context] for getting [WorkManager]
     */
    @ApplicationContext applicationContext: Context,

    /**
     * gRPC connection properties and configuration
     */
    private val configuration: GrpcServiceConfiguration,

    ) : IDiagnosisUploadService {

    /**
     * [WorkManager] instance for queuing work
     */
    private val workManager = WorkManager.getInstance(applicationContext)

    /**
     * gRPC client for diagnosis upload
     */
    val client: DiagnosesServiceClient = configuration.client.create()

    override suspend fun upload(diagnosis: Diagnosis): Result<StatusResponse> =
        withContext(Dispatchers.IO) {
            try {
                withTimeout(Duration.ofSeconds(configuration.timeoutSec)) {
                    Result.success(client.StoreDiagnosis().execute(diagnosis))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun uploadAsync(diagnosis: Diagnosis) {
        workManager.enqueue(
            OneTimeWorkRequestBuilder<DiagnosisUploadWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .setInputData(
                    workDataOf(
                        DiagnosisUploadWorker.DIAGNOSIS_KEY to diagnosis.id
                    )
                )
                .build()
        )
    }
}