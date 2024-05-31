package com.leishmaniapp.usecases

import com.leishmaniapp.entities.Diagnosis
import java.io.File

fun interface IDiagnosisSharing {
    /**
     * Create a file from a [Diagnosis] for sharing
     * TODO: Use typed exceptions
     */
    suspend fun generateDiagnosisFile(diagnosis: Diagnosis): File
}