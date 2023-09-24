package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.DiagnosticElementRoom

@Dao
interface DiagnosticElementDao {
    @Upsert
    suspend fun upsertDiagnosticElement(diagnosticElement: DiagnosticElementRoom)

    @Delete
    suspend fun deleteDiagnosticElement(diagnosticElement: DiagnosticElementRoom)

    @Query("SELECT * FROM diagnosticelementroom")
    suspend fun getDiagnosticElement(): List<DiagnosticElementRoom>

}