package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.Username

@Dao
interface SpecialistDao {
    @Upsert
    suspend fun upsertSpecialist(specialist: Specialist)

    @Delete
    suspend fun deleteSpecialist(specialist: Specialist)

    @Query("SELECT * FROM specialist")
    suspend fun allSpecialists(): List<Specialist>

    @Query("SELECT * FROM specialist WHERE username = :username")
    suspend fun specialistByUsername(username: Username): Specialist?

    @Query("SELECT * FROM specialist WHERE username = :username AND password = :password")
    suspend fun specialistByCredentials(username: Username, password: Password): Specialist?
}