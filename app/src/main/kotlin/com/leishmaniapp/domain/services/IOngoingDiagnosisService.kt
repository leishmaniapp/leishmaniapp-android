package com.leishmaniapp.domain.services

import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.types.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

/**
 * Get the currently ongoing [Diagnosis]
 */
interface IOngoingDiagnosisService {

    /**
     * Set the ongoing disease
     */
    suspend fun setOngoingDiagnosis(specialist: Email, uuid: UUID): Result<Unit>

    /**
     * Get the current diagnosis (ongoing)
     */
    suspend fun getOngoingDiagnosis(specialist: Email): Flow<UUID?>

    /**
     * Delete the current ongoing disease
     */
    suspend fun removeOngoingDiagnosis(specialist: Email): Result<Unit>

}