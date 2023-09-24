package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.DiagnosisRoom
import java.time.LocalDateTime

@Dao
interface DiagnosisDao {
    @Upsert
    suspend fun upsetDiagnosis(diagnosis: DiagnosisRoom)
    @Delete
    suspend fun deleteDiagnosis(diagnosis: DiagnosisRoom)
    @Query("SELECT * FROM diagnosisroom WHERE date = :searchDate")
    suspend fun getDiagnosisByDate(searchDate: LocalDateTime): List<DiagnosisRoom>

    @Query("SELECT * FROM diagnosisroom")
    suspend fun getAllDiagnoses(): List<DiagnosisRoom>
}