package com.leishmaniapp.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Check if remote service is available using health server
 */
interface IAvailabilityService {

    /**
     * One-shot request at the health server
     */
    suspend fun checkServiceAvailability(): Boolean

    /**
     * Streamed request at the health server, results are pushed to [isServiceAvailable]
     */
    fun restartStreamServiceAvailability()

    /**
     * [StateFlow] with a [Boolean] flag to check if the remote server is available or not
     */
    val isServiceAvailable: StateFlow<Boolean>
}