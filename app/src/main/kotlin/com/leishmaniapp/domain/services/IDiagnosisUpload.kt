package com.leishmaniapp.domain.services

import com.leishmaniapp.cloud.model.Diagnosis
import com.leishmaniapp.cloud.types.StatusResponse

/**
 * Upload a diagnosis to the remote service
 */
fun interface IDiagnosisUpload {
    suspend fun uploadDiagnosis(diagnosis: Diagnosis): Result<StatusResponse>
}