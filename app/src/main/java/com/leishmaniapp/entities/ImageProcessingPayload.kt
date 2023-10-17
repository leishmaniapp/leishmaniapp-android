package com.leishmaniapp.entities

import com.leishmaniapp.entities.disease.Disease
import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Cloud-based representation of an [Image]
 */
@Serializable
data class ImageProcessingPayload(
    val id: String,
    val sample: Int,
    val date: String,
    val size: Int,
    val processed: Boolean,
    val disease: String,
    val diagnosis: String,
    val reference: String,
    val models: List<String>
) {
    companion object {
        fun Image.toProcessingPayload(diagnosisId: UUID, disease: Disease, reference: String) =
            ImageProcessingPayload(
                id = String.format("%d@%s", this.sample, diagnosisId.toString()),
                sample = this.sample,
                date = this.date.toString(),
                size = this.size,
                processed = this.processed == ImageAnalysisStatus.Analyzed,
                disease = disease.id,
                diagnosis = diagnosisId.toString(),
                reference = reference,
                models = disease.models.map { it.value }
            )
    }
}
