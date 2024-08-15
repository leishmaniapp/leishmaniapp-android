package com.leishmaniapp.infrastructure.service.analysis

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.repository.ISamplesRepository
import com.leishmaniapp.domain.services.IAvailabilityService
import com.leishmaniapp.domain.services.ILocalAnalysisService
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

    /**
     * [Context] for [WorkManager]
     */
    @ApplicationContext applicationContext: Context,

    // Repositories
    private val samplesRepository: ISamplesRepository,

    // Services
    private val networkService: IAvailabilityService,
    private val localAnalysisService: ILocalAnalysisService,

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
     * Pass the image to either the [RemoteAnalysisQueuingWorker] or a [ILocalAnalysisService]
     */
    override suspend fun enqueue(sample: ImageSample, specialist: Email, mime: String) {

        // Set the image as Enqueued state
        withContext(Dispatchers.IO) {
            samplesRepository.upsertSample(sample.copy(stage = AnalysisStage.Enqueued))
        }

        // Check if remote service is available
        val shouldContinueAnalysis = if (!networkService.checkServiceAvailability()) {

            // Check if LAM is available
            localAnalysisService.tryAnalyze(sample).let { result ->

                // Handle analysis failure or false as return value
                if (result.isFailure || !result.getOrDefault(false)) {

                    // Show an error or a warning depending on the severity
                    result.onFailure {
                        Log.e(TAG, "Failed to analyze image locally", it)
                    }.onSuccess {
                        Log.w(TAG, "LAM module not installed for (${sample.metadata.disease})")
                    }

                    // Mark the sample as deferred
                    withContext(Dispatchers.IO) {
                        samplesRepository.upsertSample(sample.copy(stage = AnalysisStage.Deferred))
                    }

                    // Should continue analysis as LAM cannot be used
                    Log.d(
                        TAG,
                        "Deferred cloud analysis will be used as LAM could not be found"
                    )
                    true

                } else {

                    // Should not continue the analysis as LAM was used
                    Log.d(
                        TAG,
                        "LAM analysis will be used as the online service is not available"
                    )
                    false
                }
            }

        } else {
            // Should continue analysis as the service is currently online
            Log.d(TAG, "Cloud analysis service will be used as preferred method of analysis")
            true
        }

        // Complete the analysis request right now
        if (!shouldContinueAnalysis) {
            Log.d(
                TAG,
                "Request ended without a request to [RemoteAnalysisQueuingWorker]"
            )
            return
        }

        // Enqueue the online worker
        workManager.enqueue(
            OneTimeWorkRequestBuilder<RemoteAnalysisQueuingWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
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