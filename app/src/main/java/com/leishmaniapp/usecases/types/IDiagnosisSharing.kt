package com.leishmaniapp.usecases.types

import com.leishmaniapp.entities.Diagnosis
import java.io.File

interface IDiagnosisSharing {
    suspend fun prepareDiagnosisForSharing(diagnosis: Diagnosis): File
}