package com.leishmaniapp.infrastructure.service.cloud

import android.util.Log
import com.leishmaniapp.cloud.analysis.AnalysisRequest
import com.leishmaniapp.cloud.analysis.AnalysisResponse
import com.leishmaniapp.cloud.analysis.AnalysisServiceClient
import com.leishmaniapp.domain.services.IAnalysisService
import com.leishmaniapp.infrastructure.di.InjectScopeWithIODispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

/**
 * gRPC implementation of [IAnalysisService]
 */
class GrpcAnalysisServiceImpl @Inject constructor(

    @InjectScopeWithIODispatcher
    private val coroutineScope: CoroutineScope,

    /**
     * gRPC connection properties and configuration
     */
    configuration: GrpcServiceConfiguration,

    ) : IAnalysisService {

    companion object {
        /**
         * TAG for using with [Log]
         */
        val TAG: String = GrpcAnalysisServiceImpl::class.simpleName!!
    }

    /**
     * Protobuf representation of [AnalysisServiceClient]
     */
    private val client: AnalysisServiceClient = configuration.client.create()

    /**
     * Channel for sending async requests
     */
    private var sendChannel: SendChannel<AnalysisRequest>

    /**
     * Channel for recieving async responses
     */
    private var recvChannel: ReceiveChannel<AnalysisResponse>

    /**
     * [Flow] with the [AnalysisResponse] pushed by the server stream
     */
    override val results: Flow<AnalysisResponse>
        get() = recvChannel.receiveAsFlow()

    init {
        // Create the channels
        val (send, recv) = client.StartAnalysis().executeIn(coroutineScope)

        // Add the channels to current variables
        sendChannel = send
        recvChannel = recv
    }

    override suspend fun analyze(request: AnalysisRequest): Result<Unit> =
        coroutineScope {
            try {
                sendChannel.send(request)
                return@coroutineScope Result.success(Unit)
            } catch (e: Exception) {
                return@coroutineScope Result.failure(e)
            }
        }

    override suspend fun reset(): Result<Unit> =
        try {
            // Close previous channels
            sendChannel.close()

            // Create the channels
            val (send, recv) = client.StartAnalysis().executeIn(coroutineScope)

            // Add the channels to current variables
            sendChannel = send
            recvChannel = recv

            // Return success
            Log.i(TAG, "Reset on AnalysisService stream")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Cannot create new AnalysisServer stream", e)
            Result.failure(e)
        }
}
