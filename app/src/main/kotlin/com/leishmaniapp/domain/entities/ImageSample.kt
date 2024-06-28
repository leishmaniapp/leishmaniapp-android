package com.leishmaniapp.domain.entities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
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
    val file: Uri? = null,

    ) : Parcelable {

    companion object {
        /**
         * Standarized picture size, both height and width
         */
        const val STD_IMAGE_RESOLUTION = 1944
    }

    /**
     * Construct the next [ImageSample] in a [Diagnosis]
     */
    constructor(diagnosis: Diagnosis, file: Uri) : this(
        file = file,
        metadata = ImageMetadata(
            diagnosis = diagnosis.id,
            sample = diagnosis.samples,
            disease = diagnosis.disease,
        ),
    )

    /**
     * Get the [Bitmap] associated to the sample
     */
    @IgnoredOnParcel
    val bitmap by lazy { file?.let { file -> BitmapFactory.decodeFile(file.path) } }
}