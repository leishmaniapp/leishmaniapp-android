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
@Dao
interface SpecialistsRepository {

    /**
     * Insert or update a [Specialist]
     */
    @Upsert
    suspend fun upsertSpecialist(specialist: Specialist)

    /**
     * Delete a [Specialist] from database
     */
    @Delete
    suspend fun deleteSpecialist(specialist: Specialist)

    /**
     * Get a [Specialist] given its [Email]
     */
    @Query("SELECT * FROM specialist WHERE email = :email")
    suspend fun specialistByUsername(email: Email): Specialist?

    /**
     * Delete a [Specialist] given its [Email]
     */
    @Query("DELETE FROM Specialist WHERE email = :email")
    suspend fun deleteSpecialistByUsername(email: Email)

    /**
     * Get all the [Specialist] stored in database
     */
    @Query("SELECT * FROM specialist")
    suspend fun allSpecialists(): Flow<List<Specialist>>
}