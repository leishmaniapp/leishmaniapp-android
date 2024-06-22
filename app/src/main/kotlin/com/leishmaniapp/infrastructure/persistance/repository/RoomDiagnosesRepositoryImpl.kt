package com.leishmaniapp.infrastructure.persistance.repository

import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.entities.Patient
import com.leishmaniapp.domain.repository.IDiagnosesRepository
import com.leishmaniapp.domain.types.Email
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
    val dao: RoomDiagnosesDao,

    ) : IDiagnosesRepository {

    override suspend fun upsertDiagnosis(diagnosis: Diagnosis) = dao.upsertDiagnosis(
        RoomDiagnosisEntity(diagnosis)
    )

    override suspend fun deleteDiagnosis(diagnosis: Diagnosis) = dao.deleteDiagnosis(
        RoomDiagnosisEntity(diagnosis)
    )

    override fun allDiagnoses(): Flow<List<Diagnosis>> =
        dao.allDiagnoses().map { flow -> flow.map { it.toDiagnosis() } }

    override fun diagnosesForPatient(patient: Patient): Flow<List<Diagnosis>> =
        dao.diagnosesForPatientDocument(patient.document)
            .map { flow -> flow.map { it.toDiagnosis() } }

    override fun diagnosesForSpecialistNotFinished(specialistEmail: Email): Flow<List<Diagnosis>> =
        dao.diagnosesForSpecialistNotFinished(specialistEmail)
            .map { flow -> flow.map { it.toDiagnosis() } }

    override fun diagnosisForId(uuid: UUID): Flow<Diagnosis?> =
        dao.diagnosisForId(uuid).map { it?.toDiagnosis() }
}