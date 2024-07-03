package com.leishmaniapp.infrastructure.work

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
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
import com.leishmaniapp.utilities.extensions.getOrThrow
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@HiltWorker
class ImageResultsWorker @AssistedInject constructor(

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


        val TAG: String = ImageResultsWorker::class.simpleName!!
    }

    /**
     * Get the system [NotificationManager] for foreground service
     */
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    /**
     * Foreground service persistent notification
     */
    private val workerNotification: Notification = NotificationCompat.Builder(
        applicationContext, applicationContext.getString(R.string.notification_analysis_channel)
    ).setContentTitle(applicationContext.getString(R.string.notification_analysis_title))
        .setContentText(applicationContext.getString(R.string.notification_analysis_content))
        .setOngoing(true).build()

    override suspend fun doWork(): Result {

        Log.i(TAG, "Analysis results are being fetched")

        // Move the coroutine into the context of a foreground service
        setForeground(
            ForegroundInfo(
                NOTIFICATION_ID,
                workerNotification
            )
        )

        // Collect the results
        withContext(Dispatchers.IO) {
            analysisService.results().collect { response ->
                response.status!!.code.asResult { response.ok!! }.fold(
                    onSuccess = { v ->
                        // Parse the image metadata
                        val metadata = ImageMetadata.fromProto(v.metadata!!)
                        // Get the image (or create a new one)
                        val image = samplesRepository.imageForMetadata(metadata).first()
                            ?: ImageSample(
                                metadata = metadata,
                                stage = AnalysisStage.Analyzed,
                            )
                        // Create a copy with the new elements
                        val copy = image
                            .withModelElements(ModelDiagnosticElement.from(v.results))
                            .copy(stage = AnalysisStage.Analyzed)
                        // Store the copy
                        samplesRepository.upsertImage(copy)

                    }, onFailure = { e ->
                        if (e is BadAnalysisException) {
                            // Parse the image metadata
                            val metadata = ImageMetadata.fromProto(response.error!!.metadata!!)
                            // Get the image (or create a new one)
                            val image = samplesRepository.imageForMetadata(metadata).first()
                                ?: ImageSample(
                                    metadata = metadata,
                                    stage = AnalysisStage.ResultError,
                                )
                            // Create a copy with the new stage
                            val copy = image.copy(stage = AnalysisStage.ResultError)
                            // Store the copy
                            samplesRepository.upsertImage(copy)
                        } else {
                            Log.e(TAG, "Unexpected error response for image", e)
                        }
                    })
            }
        }

        return Result.success()
    }
}