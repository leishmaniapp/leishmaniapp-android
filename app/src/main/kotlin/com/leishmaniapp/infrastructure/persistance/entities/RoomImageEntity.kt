package com.leishmaniapp.infrastructure.persistance.entities

import android.graphics.Bitmap
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.DiagnosticElement
import com.leishmaniapp.domain.entities.ImageMetadata
import com.leishmaniapp.domain.entities.ImageSample

/**
 * SQL representation of an [ImageSample]
 */
@Entity(
    tableName = "Images",
    primaryKeys = ["diagnosis", "sample"],
    foreignKeys = [
        ForeignKey(
            entity = RoomDiagnosisEntity::class,
            parentColumns = ["id"],
            childColumns = ["diagnosis"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class RoomImageEntity(
    @Embedded val metadata: ImageMetadata,
    val stage: AnalysisStage = AnalysisStage.NotAnalyzed,
    val elements: Set<DiagnosticElement>,
    val file: Uri?,
) {
    constructor(imageSample: ImageSample) : this(
        metadata = imageSample.metadata,
        stage = imageSample.stage,
        elements = imageSample.elements,
        file = imageSample.file,
    )

    fun toImageSample(): ImageSample = ImageSample(
        metadata = metadata,
        stage = stage,
        elements = elements,
        file = file
    )
}