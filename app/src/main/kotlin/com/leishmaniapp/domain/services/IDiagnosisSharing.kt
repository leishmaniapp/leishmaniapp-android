package com.leishmaniapp.domain.services

import com.leishmaniapp.domain.entities.Diagnosis
import java.io.File

/**
 * Create a file from a [Diagnosis] for sharing
 */
fun interface IDiagnosisSharing {

    /**
     * Share the [Diagnosis] as a file
     */
    suspend fun share(diagnosis: Diagnosis): File
}