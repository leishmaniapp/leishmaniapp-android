package com.leishmaniapp.infrastructure.persistance.repository

import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.repository.ISamplesRepository
import com.leishmaniapp.infrastructure.persistance.dao.RoomImagesDao
import com.leishmaniapp.infrastructure.persistance.entities.RoomImageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

/**
 * [ISamplesRepository] implementation for the Room database
 */
class RoomSamplesRepositoryImpl @Inject constructor(
    /**
     * DAO for interaction with the Room database
     */
    val dao: RoomImagesDao,

    ) : ISamplesRepository {

    override suspend fun upsertImage(image: ImageSample) =
        dao.upsertImage(RoomImageEntity(image))

    override suspend fun deleteImage(image: ImageSample) =
        dao.deleteImage(RoomImageEntity(image))

    override fun imageForDiagnosis(diagnosis: UUID, sample: Int): Flow<ImageSample> =
        dao.imageForDiagnosis(diagnosis, sample).map { it.toImageSample() }

    override fun allImagesForDiagnosis(diagnosis: UUID): Flow<List<ImageSample>> =
        dao.allImagesForDiagnosis(diagnosis).map { flow -> flow.map { it.toImageSample() } }

    override fun imagesForDiagnosisWithStage(
        diagnosis: UUID,
        stage: AnalysisStage
    ): Flow<List<ImageSample>> =
        dao.imagesForDiagnosisWithStage(diagnosis, stage)
            .map { flow -> flow.map { it.toImageSample() } }
}