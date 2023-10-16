package com.leishmaniapp.infrastructure

import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.usecases.types.IDiagnosisSharing
import java.io.File
import javax.inject.Inject

class PDFDiagnosisSharing @Inject constructor() : IDiagnosisSharing {
    override suspend fun prepareDiagnosisForSharing(diagnosis: Diagnosis): File {
        TODO("Not yet implemented")
    }
}