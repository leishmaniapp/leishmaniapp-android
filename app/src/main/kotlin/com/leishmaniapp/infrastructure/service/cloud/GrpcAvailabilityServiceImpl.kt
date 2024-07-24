package com.leishmaniapp.infrastructure.service.cloud

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.leishmaniapp.domain.services.IAvailabilityService
import com.leishmaniapp.infrastructure.di.InjectScopeWithIODispatcher
import com.leishmaniapp.infrastructure.work.AvailabilityCheckWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import io.grpc.health.v1.HealthCheckRequest
import io.grpc.health.v1.HealthCheckResponse
import io.grpc.health.v1.HealthClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * [IAvailabilityService] using Android SDK tools
 */
class GrpcAvailabilityServiceImpl @Inject constructor(

    /**
     * [Context] for getting [WorkManager]
     */
    @ApplicationContext applicationContext: Context,

    /**
     * gRPC connection properties and configuration
     */
    configuration: GrpcServiceConfiguration,

    /**
     * [CoroutineScope] for running [Flow] on [HealthClient]
     */
    @InjectScopeWithIODispatcher val coroutineScope: CoroutineScope,

    ) : IAvailabilityService {

    companion object {
        /**
         * TAG for using with [Log] for gRPC streams
         */
        const val STREAMED_TAG: String = "StreamedHealthCheck"

        /**
         * TAG for using with [Log] for gRPC streams
         */
        const val UNICAST_TAG: String = "UnicastHealthCheck"

        /**
         * Retry time in milliseconds
         */
        const val RETRY_MILLIS: Long = 30_000

        /**
         * id for the unique [AvailabilityCheckWorker]
         */
        const val AVAILABILITY_WORKER: String = "availability_worker";
    }

    /**
     * [WorkManager] instance for queuing work
     */
    private val workManager = WorkManager.getInstance(applicationContext)

    /**
     * Create an instance of [AvailabilityCheckWorker] to automatically call [restartStreamServiceAvailability]
     */
    private fun automaticStreamServiceAvailabilityRestart() {
        workManager.enqueueUniqueWork(
            AVAILABILITY_WORKER,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<AvailabilityCheckWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                ).setBackoffCriteria(
                    BackoffPolicy.LINEAR, RETRY_MILLIS, TimeUnit.MILLISECONDS
                ).build()
        )
    }

    init {
        automaticStreamServiceAvailabilityRestart()
    }


    /**
     * gRPC client for authentication
     */
    val client: HealthClient = configuration.client.create()

    override suspend fun checkServiceAvailability(): Boolean = try {
        val status = client.Check().execute(HealthCheckRequest()).status
        Log.i(UNICAST_TAG, "Success with status ($status)")
        // Check if status is SERVING
        (status == HealthCheckResponse.ServingStatus.SERVING)
    } catch (e: Exception) {
        Log.w(UNICAST_TAG, "Remote service not available", e)
        false
    }

    private lateinit var healthSendChannel: SendChannel<HealthCheckRequest>
    private lateinit var healthRecvChannel: ReceiveChannel<HealthCheckResponse>

    private var _isServiceAvailable: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val isServiceAvailable: StateFlow<Boolean> = _isServiceAvailable

    override fun restartStreamServiceAvailability() {
        try {
            // Create the communication channel
            val (snd, recv) = client.Watch().executeIn(coroutineScope)
            Log.d(STREAMED_TAG, "Initialized bidirectional communication channel")

            // Set the variables
            healthSendChannel = snd
            healthRecvChannel = recv

            coroutineScope.launch {
                // Send a request
                healthSendChannel.send(HealthCheckRequest())
                // Restart the flow
                healthRecvChannel.consumeAsFlow()
                    .onEach { status -> Log.i(STREAMED_TAG, status.toString()) }
                    .map { r -> (r.status == HealthCheckResponse.ServingStatus.SERVING) }
                    .catch { e ->
                        Log.w(STREAMED_TAG, "Remote service closed connection", e)
                        automaticStreamServiceAvailabilityRestart()
                        _isServiceAvailable.value = false
                    }
                    .collect { value ->
                        // Change the value in the state flow
                        _isServiceAvailable.value = value
                    }
            }

        } catch (e: Exception) {
            Log.w(STREAMED_TAG, "Remote service not available", e)
            automaticStreamServiceAvailabilityRestart()
            _isServiceAvailable.value = false
        }
    }
}