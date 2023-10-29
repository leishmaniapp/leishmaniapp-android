package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.DocumentType
import com.leishmaniapp.entities.IdentificationDocument
import com.leishmaniapp.entities.Patient
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Upsert
    suspend fun upsertPatient(patient: Patient)

    @Query("SELECT * FROM Patient P WHERE P.documentType = :documentType AND P.id = :id")
    suspend fun patientById(
        id: IdentificationDocument, documentType: DocumentType
    ): Patient?

    @Delete
    suspend fun deletePatient(patient: Patient)

    @Query("SELECT * FROM Patient")
    suspend fun allPatients(): List<Patient>

    @Query("SELECT * FROM Patient")
    fun allPatientsFlow(): Flow<List<Patient>>
}