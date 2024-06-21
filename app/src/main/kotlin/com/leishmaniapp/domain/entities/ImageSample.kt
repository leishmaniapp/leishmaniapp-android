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
@Parcelize
data class ImageSample(
    /**
     * Associated metadata to this image
     */
    val metadata: ImageMetadata,

    /**
     * Current analysis stage
     */
    val stage: AnalysisStage = AnalysisStage.NotAnalyzed,

    /**
     * Associated diagnostic elements to this sample
     */
    val elements: Set<DiagnosticElement> = setOf(),

    /**
     * Local path to the image file inside local storage
     */
    val path: Uri? = null,

    ) : Parcelable {

    companion object {
        /**
         * Standarized picture size, both height and width
         */
        const val STD_IMAGE_RESOLUTION = 1944
    }

}