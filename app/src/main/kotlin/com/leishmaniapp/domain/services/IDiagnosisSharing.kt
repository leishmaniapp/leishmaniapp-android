package com.leishmaniapp.domain.services

import com.leishmaniapp.domain.entities.Diagnosis
import java.io.File

/**
 * Create a file from a [Diagnosis] for sharing
 */
fun interface IDiagnosisSharing {
    suspend fun generateDiagnosisFile(diagnosis: Diagnosis): File
}