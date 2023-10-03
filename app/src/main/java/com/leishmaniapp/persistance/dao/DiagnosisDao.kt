package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.DiagnosisRoom
import com.leishmaniapp.entities.DocumentType
import com.leishmaniapp.entities.IdentificationDocument
import com.leishmaniapp.entities.Patient

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


}