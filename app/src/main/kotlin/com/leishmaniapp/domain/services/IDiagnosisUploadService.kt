package com.leishmaniapp.domain.services

import com.leishmaniapp.cloud.model.Diagnosis
import com.leishmaniapp.cloud.types.StatusResponse

/**
 * Upload a diagnosis to the remote service
 * This service is based upon the LeishmaniappCloudServicesv2 definition
 * [visit protobuf_schema for more information](https://github.com/leishmaniapp/protobuf_schema)
 */
interface IDiagnosisUploadService {

    /**
     * Upload a diagnosis
     */
    suspend fun upload(diagnosis: Diagnosis): Result<StatusResponse>

    /**
     * Upload a diagnosis within an async worker
     */
    suspend fun uploadAsync(diagnosis: Diagnosis)
}