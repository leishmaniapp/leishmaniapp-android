package com.leishmaniapp.domain.entities

import android.os.Parcelable
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.parceler.LocalDateTimeParceler
import com.leishmaniapp.utilities.extensions.utcNow
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

    ) : Parcelable {

    companion object;

}
