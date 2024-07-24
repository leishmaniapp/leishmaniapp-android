package com.leishmaniapp.domain.services

import com.leishmaniapp.domain.entities.Diagnosis
import java.io.File

/**
 * Create a file from a [Diagnosis] for sharing
 */
interface IDiagnosisSharingService {

    /**
     * MIME type for resulting files
     */
    val mime: String

    /**
     * Share the [Diagnosis] as a file
     */
    suspend fun share(diagnosis: Diagnosis): File
}