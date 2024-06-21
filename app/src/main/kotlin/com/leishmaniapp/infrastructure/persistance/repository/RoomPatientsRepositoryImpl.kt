package com.leishmaniapp.infrastructure.persistance.repository

import com.leishmaniapp.domain.entities.DocumentType
import com.leishmaniapp.domain.entities.Patient
import com.leishmaniapp.domain.repository.IPatientsRepository
import com.leishmaniapp.domain.types.Identificator
import com.leishmaniapp.infrastructure.persistance.dao.RoomPatientsDao
import com.leishmaniapp.infrastructure.persistance.entities.RoomPatientEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * [IPatientsRepository] implementation for the Room database
 */
class RoomPatientsRepositoryImpl @Inject constructor(

    /**
     * DAO for interaction with the Room database
     */
    val dao: RoomPatientsDao,

    ) : IPatientsRepository {
    override suspend fun upsertPatient(patient: Patient) =
        dao.upsertPatient(RoomPatientEntity(patient))

    override suspend fun deletePatient(patient: Patient) =
        dao.deletePatient(RoomPatientEntity(patient))

    override fun patientById(id: Identificator, documentType: DocumentType): Flow<Patient> =
        dao.patientById(id, documentType).map { it.toPatient() }

    override fun allPatients(): Flow<List<Patient>> =
        dao.allPatients().map { flow -> flow.map { it.toPatient() } }
}