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
interface IPatientsRepository {

    /**
     * Update or insert a [Patient]
     */
    suspend fun upsertPatient(patient: Patient)

    /**
     * Delete a [Patient]
     */
    suspend fun deletePatient(patient: Patient)

    /**
     * Get a [Patient] given its [Identificator] and [DocumentType]
     */
    fun patientById(id: Identificator, documentType: DocumentType): Flow<Patient?>

    /**
     * Get all the [Patient] stored in database
     */
    fun allPatients(): Flow<List<Patient>>
}