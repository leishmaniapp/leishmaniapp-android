package com.leishmaniapp.infrastructure

import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.usecases.IDiagnosisSharing
import java.io.File
import javax.inject.Inject

class ApplicationDiagnosisSharing @Inject constructor() : IDiagnosisSharing {
    override suspend fun shareDiagnosisFile(diagnosis: Diagnosis): File {
        TODO("Not yet implemented")
    }
}