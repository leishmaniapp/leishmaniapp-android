package com.leishmaniapp.infrastructure

import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.usecases.types.IDiagnosisSharing
import java.io.File

class PDFDiagnosisSharing : IDiagnosisSharing {
    override suspend fun prepareDiagnosisForSharing(diagnosis: Diagnosis): File {
        TODO("Not yet implemented")
    }
}