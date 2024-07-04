package com.leishmaniapp.infrastructure.persistance.repository

import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.entities.Patient
import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.domain.repository.IDiagnosesRepository
import com.leishmaniapp.infrastructure.persistance.dao.RoomDiagnosesDao
import com.leishmaniapp.infrastructure.persistance.entities.RoomDiagnosisEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

/**
 * [IDiagnosesRepository] implementation for the Room database
 */
class RoomDiagnosesRepositoryImpl @Inject constructor(
    /**
     * DAO for interaction with the Room database
     */
    private val dao: RoomDiagnosesDao,

    ) : IDiagnosesRepository {

    override suspend fun upsertDiagnosis(diagnosis: Diagnosis) = dao.upsertDiagnosis(
        RoomDiagnosisEntity(diagnosis)
    )

    override suspend fun deleteDiagnosis(diagnosis: Diagnosis) = dao.deleteDiagnosis(
        RoomDiagnosisEntity(diagnosis)
    )

    override fun getDiagnosesForPatient(patient: Patient): Flow<List<Diagnosis>> =
        dao.getDiagnosesForPatientDocument(patient.document)
            .map { flow -> flow.map { it.toDiagnosis() } }

    override fun getDiagnosesForSpecialist(specialist: Specialist): Flow<List<Diagnosis>> =
        dao.getDiagnosesForSpecialistEmail(specialist.email)
            .map { flow -> flow.map { it.toDiagnosis() } }

    override fun getDiagnosis(uuid: UUID): Flow<Diagnosis?> =
        dao.getDiagnosis(uuid).map { it?.toDiagnosis() }
}