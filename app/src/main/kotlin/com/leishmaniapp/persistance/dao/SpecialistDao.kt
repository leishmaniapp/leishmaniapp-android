package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.Email

@Dao
interface SpecialistDao {
    @Upsert
    suspend fun upsertSpecialist(specialist: Specialist)

    @Delete
    suspend fun deleteSpecialist(specialist: Specialist)

    @Query("DELETE FROM Specialist WHERE email = :userEmail")
    suspend fun deleteSpecialistWithUsername(email: Email)

    @Query("SELECT * FROM specialist")
    suspend fun allSpecialists(): List<Specialist>

    @Query("SELECT * FROM specialist WHERE email = :userEmail")
    suspend fun specialistByUsername(email: Email): Specialist?

    @Query("SELECT * FROM specialist WHERE email = :userEmail AND password = :password")
    suspend fun specialistByCredentials(email: Email, password: Password): Specialist?
}