package com.leishmaniapp.domain.services

import com.leishmaniapp.cloud.analysis.AnalysisRequest
import com.leishmaniapp.cloud.analysis.AnalysisResponse
import kotlinx.coroutines.flow.Flow

/**
 * Send and recieve [AnalysisRequest] and [AnalysisResponse] to/from an analysis service
 * This service is based upon the LeishmaniappCloudServicesv2 definition
 * [visit protobuf_schema for more information](https://github.com/leishmaniapp/protobuf_schema)
 */
interface IAnalysisService {

    /**
     * Flow of [AnalysisResponse] from the backend
     */
    val results: Flow<AnalysisResponse>

    /**
     * Send a new [AnalysisRequest] for analysis
     */
    suspend fun analyze(request: AnalysisRequest): Result<Unit>

    /**
     * Restart the bidirectional stream
     */
    suspend fun reset(): Result<Unit>
}