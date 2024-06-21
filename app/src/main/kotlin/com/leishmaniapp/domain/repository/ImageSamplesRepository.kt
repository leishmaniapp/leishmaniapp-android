package com.leishmaniapp.domain.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.ImageSample
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Interface for manipulating [ImageSample] stored in database
 */
@Dao
interface ImageSamplesRepository {

    /**
     * Insert of update an [ImageSample]
     */
    @Upsert
    suspend fun upsertImage(image: ImageSample)

    /**
     * Delete an [ImageSample]
     */
    @Delete
    suspend fun deleteImage(image: ImageSample)

    /**
     * Get an [ImageSample] given a [com.leishmaniapp.domain.entities.Diagnosis] id
     */
    @Query("SELECT * FROM ImageSample I WHERE I.diagnosis = :diagnosis AND I.sample = :sample")
    suspend fun imageForDiagnosis(diagnosis: UUID, sample: Int): Flow<ImageSample>

    /**
     * Get every [ImageSample] associated to a [com.leishmaniapp.domain.entities.Diagnosis] id
     */
    @Query("SELECT * FROM ImageSample I WHERE I.diagnosis = :diagnosis")
    suspend fun allImagesForDiagnosis(diagnosis: UUID): Flow<List<ImageSample>>

    /**
     * Get every [ImageSample] associated to a [com.leishmaniapp.domain.entities.Diagnosis] id with
     * a given [AnalysisStage]
     */
    @Query("SELECT * FROM ImageSample I WHERE I.diagnosis = :diagnosis AND I.stage = :stage")
    suspend fun imagesForDiagnosisWithStage(
        diagnosis: UUID,
        stage: AnalysisStage
    ): Flow<List<ImageSample>>

}