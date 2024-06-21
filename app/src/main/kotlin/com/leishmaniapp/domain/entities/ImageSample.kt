package com.leishmaniapp.domain.entities

import android.net.Uri
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.parcelize.Parcelize

/**
 * Represents a diagnostic image (also known as sample). Internal use onlye, must not be confused with
 * [com.leishmaniapp.cloud.model.Sample] which is a completely different entity.
 * @immutable Replace by using [ImageSample.copy]
 */
@Entity(
    primaryKeys = ["diagnosis", "sample"],
    foreignKeys = [
        ForeignKey(
            entity = Diagnosis::class,
            childColumns = ["diagnosis"],
            parentColumns = ["id"]
        ),
    ]
)
@Parcelize
data class ImageSample(
    /**
     * Associated metadata to this image
     */
    @Embedded val metadata: ImageMetadata,

    /**
     * Current analysis stage
     */
    val stage: AnalysisStage = AnalysisStage.NotAnalyzed,

    /**
     * Number of sample in the diagnosis
     */
    val sample: Int,

    /**
     * Associated diagnostic elements to this sample
     */
    val elements: Set<DiagnosticElement> = setOf(),

    /**
     * Local path to the image file inside local storage
     */
    @Transient val path: Uri? = null,

    ) : Parcelable