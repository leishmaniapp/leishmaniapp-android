package com.leishmaniapp.entities

import android.net.Uri
import android.os.Parcelable
import com.leishmaniapp.usecases.serialization.LocalDateTimeTypeParceler
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Represents a diagnostic image
 * @immutable Replace by using [Image.copy]
 */
@Serializable
@Parcelize
data class Image(
    val sample: Int,
    @TypeParceler<LocalDateTime, LocalDateTimeTypeParceler> val date: LocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.UTC),
    val size: Int,
    val processed: ImageAnalysisStatus = ImageAnalysisStatus.NotAnalyzed,
    val elements: Set<DiagnosticElement> = setOf(),
    @Transient val path: Uri? = null,
) : Parcelable