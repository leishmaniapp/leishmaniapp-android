package com.leishmaniapp.infrastructure.persistance.repository

import com.leishmaniapp.domain.entities.DocumentType
import com.leishmaniapp.domain.entities.Patient
import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.domain.repository.IPatientsRepository
import com.leishmaniapp.domain.types.Identificator
import com.leishmaniapp.infrastructure.persistance.dao.RoomDiagnosesDao
import com.leishmaniapp.infrastructure.persistance.dao.RoomPatientsDao
import com.leishmaniapp.infrastructure.persistance.entities.RoomPatientEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * [IPatientsRepository] implementation for the Room database
 */
class RoomPatientsRepositoryImpl @Inject constructor(

    private val patientsDao: RoomPatientsDao,
    private val diagnosesDao: RoomDiagnosesDao,

    ) : IPatientsRepository {
    override suspend fun upsertPatient(patient: Patient) =
        patientsDao.upsertPatient(RoomPatientEntity(patient))

    override suspend fun deletePatient(patient: Patient) =
        patientsDao.deletePatient(RoomPatientEntity(patient))

    override fun patientById(id: Identificator, documentType: DocumentType): Flow<Patient?> =
        patientsDao.patientById(id, documentType).map { it?.toPatient() }
    
    override fun allPatients(): Flow<List<Patient>> =
        patientsDao.allPatients().map { flow -> flow.map { it.toPatient() } }
}