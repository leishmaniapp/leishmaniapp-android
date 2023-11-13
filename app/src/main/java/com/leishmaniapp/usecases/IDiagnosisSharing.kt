package com.leishmaniapp.usecases

import com.leishmaniapp.entities.Diagnosis
import java.io.File

fun interface IDiagnosisSharing {
    /**
     * Create a file from a [Diagnosis] for sharing
     */
    suspend fun shareDiagnosisFile(diagnosis: Diagnosis): File
}