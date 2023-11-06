package com.leishmaniapp.infrastructure.mock

import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.usecases.IDiagnosisUpload
import javax.inject.Inject

class MockDiagnosisUpload @Inject constructor() : IDiagnosisUpload {
    override suspend fun uploadDiagnosis(diagnosis: Diagnosis) {
    }
}