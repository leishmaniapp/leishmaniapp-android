package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.SpecialistRoom

@Dao
interface SpecialistDao {
    @Upsert
    suspend fun upsertSpecialist(specialist: SpecialistRoom)

    @Delete
    suspend fun deleteSpecialist(specialist: SpecialistRoom)

    @Query("SELECT * FROM specialistroom")
    suspend fun getSpecialist(): List<SpecialistRoom>
}