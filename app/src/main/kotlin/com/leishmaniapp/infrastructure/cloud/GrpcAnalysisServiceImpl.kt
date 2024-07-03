package com.leishmaniapp.infrastructure.cloud

import com.leishmaniapp.cloud.analysis.AnalysisRequest
import com.leishmaniapp.cloud.analysis.AnalysisResponse
import com.leishmaniapp.cloud.analysis.AnalysisServiceClient
import com.leishmaniapp.domain.services.IAnalysisService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

/**
 * gRPC implementation of [IAnalysisService]
 */
class GrpcAnalysisServiceImpl @Inject constructor(
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

    override suspend fun analyze(): SendChannel<AnalysisRequest> =
        coroutineScope {
            client.StartAnalysis().executeIn(this).first
        }

    override suspend fun results(): Flow<AnalysisResponse> =
        coroutineScope {
            client.StartAnalysis().executeIn(this).second.receiveAsFlow()
        }
}
