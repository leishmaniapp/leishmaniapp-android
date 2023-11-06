package com.leishmaniapp.infrastructure.background

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.leishmaniapp.persistance.database.ApplicationDatabase
import com.leishmaniapp.usecases.IDiagnosisUpload
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

@HiltWorker
class DiagnosisUploadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted val workerParameters: WorkerParameters,
    val applicationDatabase: ApplicationDatabase,
    val diagnosisUpload: IDiagnosisUpload
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {

        return try {
            Log.d(this::class.simpleName, "Initialized request")

            // Get parameters
            val diagnosisString = workerParameters.inputData.getString("diagnosis")

            // Check values
            if (diagnosisString == null) Result.failure()

            val diagnosisUuid = UUID.fromString(diagnosisString!!)

            Log.i(this::class.simpleName, "Requested upload for diagnosis $diagnosisUuid")

            withContext(Dispatchers.IO) {
                val diagnosis = applicationDatabase.diagnosisDao().diagnosisForId(diagnosisUuid)
                if (diagnosis == null) Result.failure()

                val specialist = applicationDatabase.specialistDao()
                    .specialistByUsername(diagnosis!!.specialistUsername)
                if (specialist == null) Result.failure()

                val patient = applicationDatabase.patientDao()
                    .patientById(diagnosis.patientIdDocument, diagnosis.patientIdType)
                if (patient == null) Result.failure()

                val images =
                    applicationDatabase.imageDao().allImagesForDiagnosis(diagnosisUuid).map {
                        it.asApplicationEntity()
                    }

                diagnosisUpload.uploadDiagnosis(
                    diagnosis.asApplicationEntity(
                        specialist!!,
                        patient!!,
                        images
                    )
                )
            }

            // Set the result as success
            Result.success()

        } catch (e: Exception) {
            Log.e(this::class.simpleName, "Error: $e")
            Result.retry()
        }
    }
}