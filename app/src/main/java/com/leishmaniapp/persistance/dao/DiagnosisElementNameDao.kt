package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.DiagnosticElementName

@Dao
interface DiagnosisElementNameDao {
    @Upsert
    suspend fun upsertDiagnosisElementName(diagnosisElementName: DiagnosticElementName)

    @Delete
    suspend fun deleteDiagnosisElementName(diagnosisElementName: DiagnosticElementName)

    @Query("SELECT * FROM diagnosticelementnameroom")
    suspend fun getDiagnosisEelemntName(): List<DiagnosticElementName>
}