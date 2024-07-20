package com.leishmaniapp.domain.services

import com.leishmaniapp.domain.entities.Diagnosis
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

/**
 * Get the currently ongoing [Diagnosis]
 */
interface IOngoingDiagnosisService {

    /**
     * Set the ongoing disease
     */
    suspend fun setOngoingDiagnosis(uuid: UUID): Result<Unit>

    /**
     * Get the current diagnosis (ongoing)
     */
    val ongoingDiagnosis: StateFlow<UUID?>

    /**
     * Delete the current ongoing disease
     */
    suspend fun removeOngoingDiagnosis(): Result<Unit>

}