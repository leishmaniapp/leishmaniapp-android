package com.leishmaniapp.domain.services

import com.leishmaniapp.cloud.analysis.AnalysisRequest
import com.leishmaniapp.cloud.analysis.AnalysisResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Send and recieve [AnalysisRequest] and [AnalysisResponse] to a remote service
 */
interface IAnalysisService {
    /**
     * Start an async channel for [AnalysisRequest]
     */
    suspend fun analyze(): SendChannel<AnalysisRequest>

    /**
     * Recieve async flow of [AnalysisResponse]
     */
    suspend fun results(): Flow<AnalysisResponse>
}