package com.leishmaniapp.infrastructure.cloud

import android.content.Context
import com.leishmaniapp.cloud.analysis.AnalysisRequest
import com.leishmaniapp.cloud.analysis.AnalysisResponse
import com.leishmaniapp.cloud.analysis.AnalysisServiceClient
import com.leishmaniapp.domain.services.IAnalysisService
import com.leishmaniapp.infrastructure.di.InjectIODispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * gRPC implementation of [IAnalysisService]
 */
class GrpcAnalysisServiceImpl @Inject constructor(

    @InjectIODispatcher
    coroutineScope: CoroutineScope,

    /**
     * gRPC connection properties and configuration
     */
    configuration: GrpcServiceConfiguration,

    ) : IAnalysisService {

    /**
     * Protobuf representation of [AnalysisServiceClient]
     */
    private val client: AnalysisServiceClient =
        configuration.client.create(AnalysisServiceClient::class)

    /**
     * Channel for sending async requests
     */
    private lateinit var sendChannel: SendChannel<AnalysisRequest>

    /**
     * Channel for recieving async responses
     */
    private lateinit var recvChannel: ReceiveChannel<AnalysisResponse>

    /**
     * [Flow] with the [AnalysisResponse] pushed by the server stream
     */
    override val results: Flow<AnalysisResponse> by lazy {
        recvChannel.receiveAsFlow()
    }

    init {
        // Create the channels
        val (send, recv) = client.StartAnalysis().executeIn(coroutineScope)

        // Add the channels to current variables
        sendChannel = send
        recvChannel = recv
    }

    /**
     * Send a new [AnalysisRequest]
     */
    override suspend fun analyze(request: AnalysisRequest): Result<Unit> =
        coroutineScope {
            try {
                sendChannel.send(request)
                return@coroutineScope Result.success(Unit)
            } catch (e: Exception) {
                return@coroutineScope Result.failure(e)
            }
        }
}
