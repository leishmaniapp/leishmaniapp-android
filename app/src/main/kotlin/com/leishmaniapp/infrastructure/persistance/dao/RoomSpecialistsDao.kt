package com.leishmaniapp.infrastructure.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.domain.types.Email
import com.leishmaniapp.infrastructure.persistance.entities.RoomSpecialistEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interface for manipulating [RoomSpecialistEntity] stored in database
 */
@Dao
interface RoomSpecialistsDao {

    @Upsert
    suspend fun upsertSpecialist(specialist: RoomSpecialistEntity)

    @Delete
    suspend fun deleteSpecialist(specialist: RoomSpecialistEntity)

    @Query("SELECT * FROM Specialists WHERE email = :email")
    fun specialistByUsername(email: Email): Flow<RoomSpecialistEntity>

    @Query("DELETE FROM Specialists WHERE email = :email")
    fun deleteSpecialistByUsername(email: Email)

    @Query("SELECT * FROM Specialists")
    fun allSpecialists(): Flow<List<RoomSpecialistEntity>>
}