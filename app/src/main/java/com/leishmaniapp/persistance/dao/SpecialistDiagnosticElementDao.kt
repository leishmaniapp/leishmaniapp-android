package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.SpecialistDiagnosticElementRoom

@Dao
interface SpecialistDiagnosticElementDao {

    @Upsert
    suspend fun upsertSpecialistDiagnosticElement(specialistDiagnosticElementRoom: SpecialistDiagnosticElementRoom)

    @Delete
    suspend fun deleteSpecialistDiagnosticElement(specialistDiagnosticElementRoom: SpecialistDiagnosticElementRoom)

    @Query("SELECT * FROM specialistdiagnosticelementroom")
    suspend fun getSpecialistDiagnosticElement(): List<SpecialistDiagnosticElementRoom>
}