package com.leishmaniapp.infrastructure.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.leishmaniapp.domain.services.IAvailabilityService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AvailabilityCheckWorker @AssistedInject constructor(

    @Assisted context: Context,
    @Assisted val workerParameters: WorkerParameters,

    private val availabilityService: IAvailabilityService,

    ) : CoroutineWorker(context, workerParameters) {

    companion object {
        /**
         * TAG for using with [Log]
         */
        val TAG = AvailabilityCheckWorker::class.simpleName!!
    }

    override suspend fun doWork(): Result {
        // Server is not available
        if (!availabilityService.checkServiceAvailability()) {
            Log.w(TAG, "Service not available, will retry later")
            return Result.retry()
        }

        // Restart the service
        availabilityService.restartStreamServiceAvailability()
        Log.d(TAG, "Initializing streamed server availability request")
        return Result.success()
    }
}