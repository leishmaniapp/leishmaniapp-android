package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.DiagnosisModel
import com.leishmaniapp.entities.DiagnosisModelRoom


@Dao
interface DiagnosisModelDao {
    @Upsert
    suspend fun upsertDiagnosisModel(diagnosisModel: DiagnosisModelRoom)

    @Delete
    suspend fun deleteDiagnosisModel(diagnosisModel: DiagnosisModelRoom)

    @Query("SELECT * FROM DiagnosisModelRoom")
    suspend fun getDiagnosisModel()
}