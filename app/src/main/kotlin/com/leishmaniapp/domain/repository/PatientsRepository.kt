package com.leishmaniapp.domain.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.domain.entities.DocumentType
import com.leishmaniapp.domain.entities.Patient
import com.leishmaniapp.domain.types.Identificator
import kotlinx.coroutines.flow.Flow

/**
 * Interface for manipulating [Patient] stored in database
 */
@Dao
interface PatientsRepository {

    /**
     * Update or insert a [Patient]
     */
    @Upsert
    suspend fun upsertPatient(patient: Patient)

    /**
     * Delete a [Patient]
     */
    @Delete
    suspend fun deletePatient(patient: Patient)

    /**
     * Get a [Patient] given its [Identificator] and [DocumentType]
     */
    @Query("SELECT * FROM Patient P WHERE P.document_type = :documentType AND P.id = :id")
    suspend fun patientById(id: Identificator, documentType: DocumentType): Flow<Patient>

    /**
     * Get all the [Patient] stored in database
     */
    @Query("SELECT * FROM Patient")
    suspend fun allPatients(): Flow<List<Patient>>
}