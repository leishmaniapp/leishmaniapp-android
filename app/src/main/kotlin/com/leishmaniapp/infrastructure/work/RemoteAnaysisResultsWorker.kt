package com.leishmaniapp.infrastructure.work

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.leishmaniapp.R
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.ImageMetadata
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.entities.ModelDiagnosticElement
import com.leishmaniapp.domain.exceptions.BadAnalysisException
import com.leishmaniapp.domain.protobuf.fromProto
import com.leishmaniapp.domain.repository.ISamplesRepository
import com.leishmaniapp.domain.services.IAnalysisService
import com.leishmaniapp.utilities.extensions.asResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@HiltWorker
class RemoteAnaysisResultsWorker @AssistedInject constructor(

    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,

    private val analysisService: IAnalysisService,
    private val samplesRepository: ISamplesRepository,

    ) : CoroutineWorker(context, workerParameters) {

    companion object {
        /**
         * Notification ID within the [NotificationManager]
         */
        const val NOTIFICATION_ID = 0;

        /**
         * TAG for using with [Log]
         */
        val TAG: String = RemoteAnaysisResultsWorker::class.simpleName!!
    }

    /**
     * Get the system [NotificationManager] for foreground service
     */
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    /**
     * [NotificationChannel] for emitting notificiations
     */
    private val notificationChannel: NotificationChannel = NotificationChannel(
        applicationContext.getString(R.string.notification_remote_analysis_channel_id),
        applicationContext.getString(R.string.notification_remote_analysis_channel_name),
        NotificationManager.IMPORTANCE_LOW
    )

    /**
     * Foreground service persistent notification
     */
    private val notification: Notification = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_remote_analysis_channel_id)
    ).setContentTitle(applicationContext.getString(R.string.notification_remote_analysis_title))
        .setSmallIcon(R.drawable.icon_microscope)
        .setContentText(applicationContext.getString(R.string.notification_remote_analysis_content))
        .setOngoing(true).build()

    /**
     * Show the [RemoteAnaysisResultsWorker] status
     */
    private fun showNotification() {
        // Create the notification channel
        notificationManager.createNotificationChannel(notificationChannel)
        // Show the notification
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    /**
     * Remove the persistent notification
     */
    private fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }


    override suspend fun doWork(): Result {

        Log.i(TAG, "Analysis results are being fetched")

        // Move the coroutine into the context of a foreground service
        setForeground(
            ForegroundInfo(
                NOTIFICATION_ID,
                notification
            )
        )

        // Show the notification
        showNotification()

        // Collect the results
        try {
            withContext(Dispatchers.IO) {
                analysisService.results.collect { response ->
                    response.status!!.code.asResult { response.ok!! }.fold(
                        onSuccess = { v ->
                            Log.i(
                                TAG,
                                "Got successfull response with status (${response.status}) for sample: ${response.ok?.metadata}"
                            )
                            Log.d(
                                TAG,
                                "Raw = $v"
                            )
                            // Parse the image metadata
                            val metadata = ImageMetadata.fromProto(v.metadata!!)
                            // Get the image (or create a new one)
                            val image = samplesRepository.getSampleForMetadata(metadata).first()
                                ?: ImageSample(
                                    metadata = metadata,
                                    stage = AnalysisStage.Analyzed,
                                )
                            // Gather the results
                            val results = ModelDiagnosticElement.from(v.results)
                            Log.i(TAG, results.toString())
                            // Create a copy with the new elements
                            val copy = image
                                .withModelElements(results)
                                .copy(stage = AnalysisStage.Analyzed)
                            // Store the copy
                            samplesRepository.upsertSample(copy)

                        }, onFailure = { e ->
                            Log.e(
                                TAG,
                                "Failed analysis with status (${response.status}) for sample: ${response.error?.metadata}",
                                e
                            )
                            if (e is BadAnalysisException) {
                                // Parse the image metadata
                                val metadata = ImageMetadata.fromProto(response.error!!.metadata!!)
                                // Get the image (or create a new one)
                                val image = samplesRepository.getSampleForMetadata(metadata).first()
                                    ?: ImageSample(
                                        metadata = metadata,
                                        stage = AnalysisStage.ResultError,
                                    )
                                // Create a copy with the new stage
                                val copy = image.copy(stage = AnalysisStage.ResultError)
                                // Store the copy
                                samplesRepository.upsertSample(copy)
                            } else {
                                Log.e(TAG, "Unexpected error response for image", e)
                            }
                        })
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch results", e)
            analysisService.reset()

            cancelNotification()
            return Result.retry()
        }


        cancelNotification()
        return Result.success()
    }
}