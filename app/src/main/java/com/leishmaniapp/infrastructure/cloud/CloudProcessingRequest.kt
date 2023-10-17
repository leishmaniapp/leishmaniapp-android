package com.leishmaniapp.infrastructure.cloud


import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ImageProcessingPayload
import com.leishmaniapp.usecases.IProcessingRequest
import java.util.UUID
import javax.inject.Inject

class CloudProcessingRequest @Inject constructor() : IProcessingRequest {

    override suspend fun uploadImageToBucket(
        diagnosisId: UUID, image: Image
    ): String {
        TODO("Not yet implemented")
    }

    override suspend fun generatePayloadRequest(payload: ImageProcessingPayload) {
        TODO("Not yet implemented")
    }

}