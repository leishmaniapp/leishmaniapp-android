package com.leishmaniapp.domain.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.domain.entities.Specialist
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
    fun specialistByUsername(email: Email): Flow<Specialist>

    /**
     * Delete a [Specialist] given its [Email]
     */
    fun deleteSpecialistByUsername(email: Email)

    /**
     * Get all the [Specialist] stored in database
     */
    fun allSpecialists(): Flow<List<Specialist>>
}