package com.leishmaniapp.domain.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.ImageMetadata
import com.leishmaniapp.domain.entities.ImageSample
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Interface for manipulating [ImageSample] stored in database
 */
interface ISamplesRepository {

    /**
     * Insert of update an [ImageSample]
     */
    suspend fun upsertImage(image: ImageSample)

    /**
     * Delete an [ImageSample]
     */
    suspend fun deleteImage(image: ImageSample)

    /**
     * Get an [ImageSample] given a [com.leishmaniapp.domain.entities.Diagnosis] id
     */
    fun imageForDiagnosis(diagnosis: UUID, sample: Int): Flow<ImageSample?>

    /**
     * Get an [ImageSample] given its [ImageMetadata]
     */
    fun imageForMetadata(metadata: ImageMetadata): Flow<ImageSample?>

    /**
     * Get every [ImageSample] associated to a [com.leishmaniapp.domain.entities.Diagnosis] id
     */
    fun allImagesForDiagnosis(diagnosis: UUID): Flow<List<ImageSample>>

    /**
     * Get every [ImageSample] associated to a [com.leishmaniapp.domain.entities.Diagnosis] id with
     * a given [AnalysisStage]
     */
    fun imagesForDiagnosisWithStage(
        diagnosis: UUID,
        stage: AnalysisStage
    ): Flow<List<ImageSample>>

    /**
     * Get every [ImageSample] associated to an [AnalysisStage]
     */
    fun imagesForStage(stage: AnalysisStage): Flow<List<ImageSample>>

}