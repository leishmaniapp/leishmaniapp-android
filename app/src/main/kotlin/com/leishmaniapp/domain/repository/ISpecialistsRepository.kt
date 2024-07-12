package com.leishmaniapp.domain.repository

import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.domain.types.AccessToken
import com.leishmaniapp.domain.types.Email
import kotlinx.coroutines.flow.Flow

/**
 * Interface for manipulating [Specialist] stored in database
 */
interface ISpecialistsRepository {

    /**
     * Insert or update a [Specialist]
     */
    suspend fun upsertSpecialist(specialist: Specialist)

    /**
     * Delete a [Specialist] from database
     */
    suspend fun deleteSpecialist(specialist: Specialist)

    /**
     * Get a [Specialist] given its [Email]
     */
    fun specialistByEmail(email: Email): Flow<Specialist?>

    /**
     * Delete a [Specialist] given its [Email]
     */
    fun deleteSpecialistByEmail(email: Email)

    /**
     * Get all the [Specialist] stored in database
     */
    fun allSpecialists(): Flow<List<Specialist>>
}