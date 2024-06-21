package com.leishmaniapp.domain.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.entities.DocumentType
import com.leishmaniapp.domain.types.Email
import com.leishmaniapp.domain.types.Identificator
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Interface for manipulating [Diagnosis] stored in database
 */
@Dao
interface DiagnosesRepository {

    /**
     * Insert or delete a [Diagnosis]
     */
    @Upsert
    suspend fun upsertDiagnosis(diagnosis: Diagnosis)

    /**
     * Delete a [Diagnosis] given its id
     */
    @Delete
    suspend fun deleteDiagnosis(diagnosis: Diagnosis)

    /**
     * Get all the [Diagnosis] stored in database
     */
    @Query("SELECT * FROM Diagnosis")
    suspend fun allDiagnoses(): Flow<List<Diagnosis>>

    /**
     * all the [Diagnosis] for a given [com.leishmaniapp.domain.entities.Patient]
     */
    @Query("SELECT * FROM Diagnosis DR WHERE DR.patient_id = :id AND DR.patient_document_type = :documentType")
    suspend fun diagnosesForPatient(
        id: Identificator,
        documentType: DocumentType,
    ): Flow<List<Diagnosis>>

    /**
     * List of [Diagnosis] that have not been finished for a given specialist
     */
    @Query("SELECT * FROM Diagnosis DR WHERE DR.specialist_email = :specialistEmail AND DR.finalized = 0")
    suspend fun diagnosesForSpecialistNotFinished(
        specialistEmail: Email
    ): Flow<List<Diagnosis>>

    /**
     * Get a [Diagnosis] given its [Identificator]
     */
    @Query("SELECT * FROM Diagnosis DR WHERE DR.id = :uuid")
    suspend fun diagnosisForId(uuid: UUID): Flow<Diagnosis>
}