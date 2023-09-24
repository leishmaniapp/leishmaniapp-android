package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.PatientRoom
import com.leishmaniapp.persistance.relations.PatientAndIdentificationDocument

@Dao
interface PatientDao {
    @Upsert
    suspend fun upsertPatient(patient: PatientRoom)

    @Delete
    suspend fun deletePatient(patient: PatientRoom)

    @Query("SELECT * FROM patientroom")
    suspend fun upsertPatient(): List<PatientRoom>

    @Query("SELECT * FROM patientroom where id = :id")
    suspend fun getIdentificationDocument(id: String): PatientAndIdentificationDocument
}