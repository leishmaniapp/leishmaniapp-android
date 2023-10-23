package com.leishmaniapp.usecases

import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ImageProcessingPayload
import com.leishmaniapp.entities.ImageProcessingResponse
import com.leishmaniapp.entities.ImageQueryResponse
import java.util.UUID

/**
 * Make request to process images
 */
interface IProcessingRequest {
    /**
     * Upload the image to cloud-based bucket, requires [Diagnosis.id] and [Image.sample] to create the path,
     * takes [Image.path] as the image to be uploaded
     * @throws Exception on failure
     */
    suspend fun uploadImageToBucket(diagnosisId: UUID, image: Image): Pair<String, String>

    /**
     * Generate analysis request on an already uploaded image
     * @throws Exception on failure
     */
    suspend fun generatePayloadRequest(payload: ImageProcessingPayload)

    /**
     * Check if internet connection is present
     */
    suspend fun checkIfInternetConnectionIsAvailable(): Boolean

    /**
     * Get the analysis results for all images in a diagnosis
     */
    suspend fun checkImagesProcessedForDiagnosis(diagnosisId: UUID): List<ImageProcessingResponse>

    /**
     * Get the analysis results for an specific image
     */
    suspend fun checkImageResults(diagnosisId: UUID, imageSample: Int): ImageQueryResponse
}