package com.leishmaniapp.domain.entities

import android.os.Parcelable
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.utilities.time.fromUnixToLocalDateTime
import com.leishmaniapp.utilities.time.toUnixTime
import com.leishmaniapp.utilities.extensions.utcNow
import com.leishmaniapp.domain.entities.protobuf.ProtobufCompatibleEntity
import com.leishmaniapp.domain.parceler.LocalDateTimeParceler
import kotlinx.datetime.LocalDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import java.util.UUID

/**
 * Represents a sample metadata
 */
@Parcelize
data class ImageMetadata(

    /**
     * Diagnosis UUID data
     */
    val diagnosis: UUID,

    /**
     * Number of samble in diagnosis
     */
    val sample: Int,

    /**
     * Disease associated to the sample image
     */
    val disease: Disease,

    /**
     * UTC time at which the image was taken
     */
    @TypeParceler<LocalDateTime, LocalDateTimeParceler> val date: LocalDateTime = LocalDateTime.utcNow(),

    ) : Parcelable,
    ProtobufCompatibleEntity<ImageMetadata, com.leishmaniapp.cloud.model.ImageMetadata> {
    override fun fromProto(from: com.leishmaniapp.cloud.model.ImageMetadata): ImageMetadata =
        ImageMetadata(
            diagnosis = UUID.fromString(from.diagnosis),
            sample = from.sample,
            disease = Disease.diseaseById(from.disease)!!,
            date = from.date.fromUnixToLocalDateTime()
        )

    override fun toProto(): com.leishmaniapp.cloud.model.ImageMetadata =
        com.leishmaniapp.cloud.model.ImageMetadata(
            diagnosis = diagnosis.toString(),
            sample = sample,
            disease = disease.id,
            date = date.toUnixTime(),
        )
}
