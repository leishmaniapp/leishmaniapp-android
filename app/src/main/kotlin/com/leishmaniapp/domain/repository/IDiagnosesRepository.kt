package com.leishmaniapp.domain.repository

import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.entities.Patient
import com.leishmaniapp.domain.entities.Specialist
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
     * Get a [Diagnosis] given its [Identificator]
     */
    fun getDiagnosis(uuid: UUID): Flow<Diagnosis?>

    /**
     * all the [Diagnosis] for a given [Patient]
     */
    fun getDiagnosesForPatient(
        patient: Patient
    ): Flow<List<Diagnosis>>

    /**
     * all the [Diagnosis] for a given [Specialist]
     */
    fun getDiagnosesForSpecialist(
        specialist: Specialist
    ): Flow<List<Diagnosis>>

    /**
     * All the [Diagnosis.background] diagnoses not [Diagnosis.background] for a given [Specialist]
     */
    fun getAwaitingDiagnosesForSpecialist(
        specialist: Email
    ): Flow<List<Diagnosis>>
}