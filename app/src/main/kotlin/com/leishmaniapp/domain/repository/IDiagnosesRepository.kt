package com.leishmaniapp.domain.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.entities.DocumentType
import com.leishmaniapp.domain.entities.Patient
import com.leishmaniapp.domain.types.Email
import com.leishmaniapp.domain.types.Identificator
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Interface for manipulating [Diagnosis] stored in database
 */
interface IDiagnosesRepository {

    /**
     * Insert or delete a [Diagnosis]
     */
    suspend fun upsertDiagnosis(diagnosis: Diagnosis)

    /**
     * Delete a [Diagnosis] given its id
     */
    suspend fun deleteDiagnosis(diagnosis: Diagnosis)

    /**
     * Get all the [Diagnosis] stored in database
     */
    fun allDiagnoses(): Flow<List<Diagnosis>>

    /**
     * all the [Diagnosis] for a given [com.leishmaniapp.domain.entities.Patient]
     */
    fun diagnosesForPatient(
        patient: Patient
    ): Flow<List<Diagnosis>>

    /**
     * List of [Diagnosis] that have not been finished for a given specialist
     */
    fun diagnosesForSpecialistNotFinished(
        specialistEmail: Email
    ): Flow<List<Diagnosis>>

    /**
     * Get a [Diagnosis] given its [Identificator]
     */
    fun diagnosisForId(uuid: UUID): Flow<Diagnosis?>
}