package com.leishmaniapp.infrastructure.persistance.repository

import androidx.core.net.toFile
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.ImageMetadata
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.repository.ISamplesRepository
import com.leishmaniapp.infrastructure.persistance.dao.RoomImagesDao
import com.leishmaniapp.infrastructure.persistance.entities.RoomImageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

/**
 * [ISamplesRepository] implementation for the Room database
 */
class RoomSamplesRepositoryImpl @Inject constructor(
    /**
     * DAO for interaction with the Room database
     */
    private val dao: RoomImagesDao,

    ) : ISamplesRepository {

    override suspend fun upsertSample(image: ImageSample) =
        dao.upsertImage(RoomImageEntity(image))

    override suspend fun deleteSample(image: ImageSample) = withContext(Dispatchers.IO) {
        // Delete the file
        image.file?.toFile()?.delete()
        dao.deleteImage(RoomImageEntity(image))
    }

    override fun getSampleForDiagnosis(diagnosis: UUID, sample: Int): Flow<ImageSample?> =
        dao.imageForDiagnosis(diagnosis, sample).map { it?.toImageSample() }

    override fun getSampleForMetadata(metadata: ImageMetadata): Flow<ImageSample?> =
        dao.imageForDiagnosis(metadata.diagnosis, metadata.sample).map { it?.toImageSample() }

    override fun getAllSamplesForDiagnosis(diagnosis: UUID): Flow<List<ImageSample>> =
        dao.allImagesForDiagnosis(diagnosis).map { flow -> flow.map { it.toImageSample() } }

    override fun getAllSamplesForDiagnosisWithStage(
        diagnosis: UUID,
        stage: AnalysisStage
    ): Flow<List<ImageSample>> =
        dao.imagesForDiagnosisWithStage(diagnosis, stage)
            .map { flow -> flow.map { it.toImageSample() } }

    override fun getAllSamplesForStage(stage: AnalysisStage): Flow<List<ImageSample>> =
        dao.imagesForStage(stage).map { list -> list.map { it.toImageSample() } }

    override fun getAllSamples(): Flow<List<ImageSample>> =
        dao.allImages().map { list -> list.map { it.toImageSample() } }
}