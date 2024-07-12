package com.leishmaniapp.domain.repository

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
    suspend fun upsertSample(image: ImageSample)

    /**
     * Delete an [ImageSample]
     */
    suspend fun deleteSample(image: ImageSample)

    /**
     * Get an [ImageSample] given a [com.leishmaniapp.domain.entities.Diagnosis] id
     */
    fun getSampleForDiagnosis(diagnosis: UUID, sample: Int): Flow<ImageSample?>

    /**
     * Get an [ImageSample] given its [ImageMetadata]
     */
    fun getSampleForMetadata(metadata: ImageMetadata): Flow<ImageSample?>

    /**
     * Get every [ImageSample] associated to a [com.leishmaniapp.domain.entities.Diagnosis] id
     */
    fun getAllSamplesForDiagnosis(diagnosis: UUID): Flow<List<ImageSample>>

    /**
     * Get every [ImageSample] associated to a [com.leishmaniapp.domain.entities.Diagnosis] id with
     * a given [AnalysisStage]
     */
    fun getAllSamplesForDiagnosisWithStage(
        diagnosis: UUID,
        stage: AnalysisStage
    ): Flow<List<ImageSample>>

    /**
     * Get every [ImageSample] associated to an [AnalysisStage]
     */
    fun getAllSamplesForStage(stage: AnalysisStage): Flow<List<ImageSample>>

    /**
     * Get all the [ImageSample] in database
     */
    fun getAllSamples(): Flow<List<ImageSample>>
}