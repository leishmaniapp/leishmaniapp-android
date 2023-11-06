package com.leishmaniapp.infrastructure.mock

import com.leishmaniapp.entities.Coordinates
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ImageProcessingPayload
import com.leishmaniapp.entities.ImageProcessingResponse
import com.leishmaniapp.entities.ImageQueryResponse
import com.leishmaniapp.entities.disease.MockDisease
import com.leishmaniapp.usecases.IProcessingRequest
import java.util.UUID
import javax.inject.Inject

class MockProcessingRequest @Inject constructor() : IProcessingRequest {
    override suspend fun uploadImageToBucket(
        diagnosisId: UUID, image: Image
    ): Pair<String, String> {
        return "bucket" to "${image.sample}@${diagnosisId}"
    }

    override suspend fun generatePayloadRequest(payload: ImageProcessingPayload) {
    }

    override suspend fun checkIfInternetConnectionIsAvailable(): Boolean {
        return true
    }

    override suspend fun checkImagesProcessedForDiagnosis(diagnosisId: UUID): List<ImageProcessingResponse> {
        return listOf(
            ImageProcessingResponse(
                0, mapOf(
                    MockDisease.models.first() to listOf(
                        Coordinates(0, 0),
                        Coordinates(1, 1),
                    )
                )
            )
        )
    }

    override suspend fun checkImageResults(
        diagnosisId: UUID, imageSample: Int
    ): ImageQueryResponse {
        return ImageQueryResponse(
            true, mapOf(
                MockDisease.models.first() to listOf(
                    Coordinates(0, 0),
                    Coordinates(1, 1),
                )
            )
        )
    }
}