package com.leishmaniapp.infrastructure.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.infrastructure.persistance.entities.RoomImageEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Interface for manipulating [RoomImageEntity] stored in database
 */
@Dao
interface RoomImagesDao {

    @Upsert
    suspend fun upsertImage(image: RoomImageEntity)

    @Delete
    suspend fun deleteImage(image: RoomImageEntity)

    @Query("SELECT * FROM Images WHERE diagnosis = :diagnosis AND sample = :sample")
    fun imageForDiagnosis(diagnosis: UUID, sample: Int): Flow<RoomImageEntity?>

    @Query("SELECT * FROM Images WHERE diagnosis = :diagnosis")
    fun allImagesForDiagnosis(diagnosis: UUID): Flow<List<RoomImageEntity>>

    @Query("SELECT * FROM Images WHERE diagnosis = :diagnosis AND stage = :stage")
    fun imagesForDiagnosisWithStage(
        diagnosis: UUID,
        stage: AnalysisStage
    ): Flow<List<RoomImageEntity>>

    @Query("SELECT * FROM Images WHERE stage = :stage")
    fun imagesForStage(stage: AnalysisStage): Flow<List<RoomImageEntity>>

    @Query("SELECT * FROM Images")
    fun allImages(): Flow<List<RoomImageEntity>>
}