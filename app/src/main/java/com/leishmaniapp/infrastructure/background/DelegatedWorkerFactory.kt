package com.leishmaniapp.infrastructure.background

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.leishmaniapp.persistance.database.ApplicationDatabase
import com.leishmaniapp.usecases.IProcessingRequest
import javax.inject.Inject

class DelegatedWorkerFactory @Inject constructor(
    val cloudProcessingRequest: IProcessingRequest,
    val applicationDatabase: ApplicationDatabase
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = when (workerClassName) {
        DiagnosisResultsWorker::class.java.name -> DiagnosisResultsWorker(
            appContext,
            workerParameters,
            cloudProcessingRequest,
            applicationDatabase
        )

        ImageProcessingWorker::class.java.name -> ImageProcessingWorker(
            appContext,
            workerParameters,
            cloudProcessingRequest,
            applicationDatabase
        )

        ImageResultsWorker::class.java.name -> ImageResultsWorker(
            appContext,
            workerParameters,
            cloudProcessingRequest,
            applicationDatabase
        )

        else -> null
    }

}