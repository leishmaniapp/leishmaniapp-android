package com.leishmaniapp.infrastructure.analysis

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.repository.ISamplesRepository
import com.leishmaniapp.domain.services.IQueuingService
import com.leishmaniapp.domain.types.Email
import com.leishmaniapp.infrastructure.work.RemoteAnalysisQueuingWorker
import com.leishmaniapp.infrastructure.work.RemoteAnaysisResultsWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * [IQueuingService] implementation using WorkManager
 */
class WorkQueuingServiceImpl @Inject constructor(

    // Android context for WorkManager
    @ApplicationContext applicationContext: Context,

    // Repositories
    private val samplesRepository: ISamplesRepository,

    ) : IQueuingService {

    companion object {

        /**
         * TAG for [Log]
         */
        val TAG: String = WorkQueuingServiceImpl::class.simpleName!!

        /**
         * Unique identifier for the analysis worker
         */
        const val RESULTS_WORKER = "analysis_enqueue_worker"
    }

    /**
     * [WorkManager] instance for queuing work
     */
    private val workManager = WorkManager.getInstance(applicationContext)

    /**
     * Start listening for results
     */
    override suspend fun startSync() {
        workManager.enqueueUniqueWork(
            RESULTS_WORKER,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<RemoteAnaysisResultsWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()
        )
    }

    /**
     * Stop listening for results
     */
    override suspend fun cancelSync() {
        workManager.cancelUniqueWork(RESULTS_WORKER)
    }

    /**
     * Pass the image to either the [RemoteAnalysisQueuingWorker] or a [?]
     * TODO: Check for internet access and defer requests
     * TODO: Use the LocalAnalysisWorker
     */
    override suspend fun enqueue(sample: ImageSample, specialist: Email, mime: String) {

        // Set the image as Enqueued state
        withContext(Dispatchers.IO) {
            samplesRepository.upsertSample(sample.copy(stage = AnalysisStage.Enqueued))
        }

        // Enqueue the worker
        Log.i(TAG, "Enqueued new sample for remote analysis")
        workManager.enqueue(
            OneTimeWorkRequestBuilder<RemoteAnalysisQueuingWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .setInputData(
                    workDataOf(
                        RemoteAnalysisQueuingWorker.SAMPLE_KEY to sample.metadata.sample,
                        RemoteAnalysisQueuingWorker.DIAGNOSIS_KEY to sample.metadata.diagnosis.toString(),
                        RemoteAnalysisQueuingWorker.SPECIALIST_KEY to specialist,
                        RemoteAnalysisQueuingWorker.MIME_KEY to mime,
                    )
                )
                .build()
        )
    }
}