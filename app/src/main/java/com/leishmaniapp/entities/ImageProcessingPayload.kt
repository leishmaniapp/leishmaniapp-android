package com.leishmaniapp.entities

import com.leishmaniapp.entities.disease.Disease
import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Cloud-based representation of an [Image]
 * {
 * 	"id": "0@dc6381da-0cab-46c0-8e08-dc7693028d6b",
 * 	"disease": "leishmaniasis-giemsa",
 * 	"reference": {
 * 		"bucket": "diagnostic-images-repository",
 * 		"key": "public/dc6381da-0cab-46c0-8e08-dc7693028d6b/0.jpg"
 * 	},
 * 	"date": "2023-10-12T15:32:45.123456Z",
 * 	"size": 1944,
 * 	"diagnosis": "dc6381da-0cab-46c0-8e08-dc7693028d6b",
 * 	"sample": 0
 * }
 */
@Serializable
data class ImageProcessingPayload(
    val id: String,
    val disease: String,
    val reference: Reference,
    val date: String,
    val size: Int,
    val diagnosis: String,
    val sample: Int,
) {
    companion object {
        @Serializable
        data class Reference(
            val bucket: String,
            val key: String
        )

        fun Image.toProcessingPayload(
            diagnosisId: UUID,
            disease: Disease,
            bucket: String,
            key: String
        ) =
            ImageProcessingPayload(
                id = String.format("%d@%s", this.sample, diagnosisId.toString()),
                disease = disease.id.replace(".", "-"),
                reference = Reference(bucket, key),
                date = this.date.toString(),
                size = this.size,
                diagnosis = diagnosisId.toString(),
                sample = this.sample,
            )
    }
}
