package com.leishmaniapp.infrastructure.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
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
    fun getAllDiagnoses(): Flow<List<RoomCompleteDiagnosisRelation>>

    @Transaction
    @Query("SELECT * FROM Diagnoses WHERE id = :uuid")
    fun getDiagnosis(uuid: UUID): Flow<RoomCompleteDiagnosisRelation?>

    @Transaction
    @Query("SELECT * FROM Diagnoses WHERE patient_document = :document")
    fun getDiagnosesForPatientDocument(
        document: String
    ): Flow<List<RoomCompleteDiagnosisRelation>>

    @Transaction
    @Query("SELECT * FROM Diagnoses WHERE specialist_email = :email")
    fun getDiagnosesForSpecialistEmail(
        email: String
    ): Flow<List<RoomCompleteDiagnosisRelation>>

    @Transaction
    @Query("SELECT * FROM Diagnoses WHERE background = 1 AND finalized = 0 AND specialist_email = :email")
    fun getBackgroundNotFinalizedDiagnosesForSpecialistEmail(
        email: String
    ): Flow<List<RoomCompleteDiagnosisRelation>>
}