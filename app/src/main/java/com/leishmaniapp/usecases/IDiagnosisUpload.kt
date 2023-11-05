package com.leishmaniapp.usecases

import com.leishmaniapp.entities.Diagnosis

interface IDiagnosisUpload {
    suspend fun uploadDiagnosis(diagnosis: Diagnosis)
}