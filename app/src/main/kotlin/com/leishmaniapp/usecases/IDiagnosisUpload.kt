package com.leishmaniapp.usecases

import com.leishmaniapp.entities.Diagnosis

/**
 * Upload a diagnosis to the remote service
 */
fun interface IDiagnosisUpload {

    /**
     * Upload the diagnosis
     * TODO: Use typed exceptions
     */
    suspend fun uploadDiagnosis(diagnosis: Diagnosis)
}