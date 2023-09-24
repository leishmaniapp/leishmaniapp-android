package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.disease.Disease
import kotlinx.coroutines.flow.Flow

@Dao
interface DiseaseDao {
    @Upsert
    suspend fun upsertDisease(disease: DiseaseDao)

    @Delete
    suspend fun deleteDisease(disease: DiseaseDao)

    @Query("SELECT * FROM diseaseroom")
    suspend fun getDiseases(): Flow<List<Disease>>
}
