package com.leishmaniapp.infrastructure.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.domain.entities.DocumentType
import com.leishmaniapp.domain.types.Identificator
import com.leishmaniapp.infrastructure.persistance.entities.RoomPatientEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interface for manipulating [RoomPatientEntity] stored in database
 */
@Dao
interface RoomPatientsDao {

    @Upsert
    suspend fun upsertPatient(patient: RoomPatientEntity)

    @Delete
    suspend fun deletePatient(patient: RoomPatientEntity)

    @Query("SELECT * FROM Patients WHERE document_type = :documentType AND id = :id")
    fun patientById(id: Identificator, documentType: DocumentType): Flow<RoomPatientEntity?>

    @Query("SELECT * FROM Patients")
    fun allPatients(): Flow<List<RoomPatientEntity>>
}