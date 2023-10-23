package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.DocumentType
import com.leishmaniapp.entities.IdentificationDocument
import com.leishmaniapp.entities.Patient
import com.leishmaniapp.entities.Username
import com.leishmaniapp.persistance.entities.DiagnosisRoom
import com.leishmaniapp.persistance.relations.DiagnosisImages
import java.util.UUID

@Dao
interface DiagnosisDao {
    @Upsert
    suspend fun upsertDiagnosis(diagnosis: DiagnosisRoom)

    @Delete
    suspend fun deleteDiagnosis(diagnosis: DiagnosisRoom)

    @Query("SELECT * FROM DiagnosisRoom")
    suspend fun allDiagnoses(): List<DiagnosisRoom>

    @Query("SELECT * FROM DiagnosisRoom DR WHERE DR.patientIdDocument = :patientId AND DR.patientIdType = :documentType")
    suspend fun diagnosesForPatientFields(
        patientId: IdentificationDocument,
        documentType: DocumentType
    ): List<DiagnosisRoom>

    suspend fun diagnosesForPatient(
        patient: Patient
    ): List<DiagnosisRoom> = diagnosesForPatientFields(
        patient.id,
        patient.documentType
    )

    //TODO: Test this method
    @Query("SELECT * FROM DiagnosisRoom DR WHERE DR.specialistUsername = :specialistUsername AND DR.finalized = 0")
    suspend fun diagnosesForSpecialistNotFinished(
        specialistUsername: Username
    ): List<DiagnosisRoom>

    //TODO: Test this method
    @Query("SELECT * FROM DiagnosisRoom DR WHERE DR.analyzed = 0")
    suspend fun diagnosesNotFinishedAnalyzed(): List<DiagnosisRoom>

    @Query("SELECT * FROM DiagnosisRoom DR WHERE DR.id = :uuid")
    suspend fun imagesForDiagnosis(uuid: UUID): DiagnosisImages

    //TODO: Test this method
    @Query("SELECT * FROM DiagnosisRoom DR WHERE DR.id = :uuid")
    suspend fun diagnosisForId(uuid: UUID): DiagnosisRoom?
}