package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.ModelDiagnosticElementRoom
import com.leishmaniapp.persistance.relations.ModelDiagnosticElemntAndDiagnosticElementName

@Dao
interface ModelDiagnosticElementDao {
    @Upsert
    suspend fun upsertModelDiagnosticElement(modelDiagnosticElement: ModelDiagnosticElementRoom)

    @Delete
    suspend fun deleteModelDiagnosticElement(modelDiagnosticElement: ModelDiagnosticElementRoom)

    @Query("SELECT * from modeldiagnosticelementroom WHERE name = :name")
    suspend fun getModelDiagnosticElement(name: String): List<ModelDiagnosticElemntAndDiagnosticElementName>

    @Query("SELECT * from modeldiagnosticelementroom")
    suspend fun getAllModelDiagnosticElements(): List<ModelDiagnosticElementRoom>
}