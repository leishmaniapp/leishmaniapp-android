package com.leishmaniapp.infrastructure.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.leishmaniapp.domain.types.Email
import com.leishmaniapp.infrastructure.persistance.entities.RoomCompleteDiagnosisRelation
import com.leishmaniapp.infrastructure.persistance.entities.RoomDiagnosisEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Interface for manipulating [RoomDiagnosisEntity] stored in database
 */
@Dao
interface RoomDiagnosesDao {
    @Upsert
    suspend fun upsertDiagnosis(diagnosis: RoomDiagnosisEntity)

    @Delete
    suspend fun deleteDiagnosis(diagnosis: RoomDiagnosisEntity)

    @Transaction
    @Query("SELECT * FROM Diagnoses WHERE 1")
    fun allDiagnoses(): Flow<List<RoomCompleteDiagnosisRelation>>

    @Transaction
    @Query("SELECT * FROM Diagnoses WHERE patient_document = :document")
    fun diagnosesForPatientDocument(
        document: String
    ): Flow<List<RoomCompleteDiagnosisRelation>>

    @Transaction
    @Query("SELECT * FROM Diagnoses WHERE specialist_email = :specialistEmail AND finalized = 0")
    fun diagnosesForSpecialistNotFinished(
        specialistEmail: Email
    ): Flow<List<RoomCompleteDiagnosisRelation>>

    @Transaction
    @Query("SELECT * FROM Diagnoses WHERE id = :uuid")
    fun diagnosisForId(uuid: UUID): Flow<RoomCompleteDiagnosisRelation>
}