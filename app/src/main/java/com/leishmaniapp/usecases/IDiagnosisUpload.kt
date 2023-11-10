package com.leishmaniapp.usecases

import com.leishmaniapp.entities.Diagnosis

fun interface IDiagnosisUpload {
    suspend fun uploadDiagnosis(diagnosis: Diagnosis)
}