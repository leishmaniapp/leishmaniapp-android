package com.leishmaniapp.entities

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import com.leishmaniapp.usecases.types.LocalDateTimeTypeParceler
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

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
    val processed: Boolean = false,
    val elements: Set<DiagnosticElement> = setOf(),
    @Transient val path: Uri? = null,
) : Parcelable

@Entity(
    primaryKeys = ["diagnosisUUID", "sample"], foreignKeys = [ForeignKey(
        entity = DiagnosisRoom::class, childColumns = ["diagnosisUUID"], parentColumns = ["id"]
    )]
)
data class ImageRoom(
    val diagnosisUUID: UUID,
    val sample: Int,
    val date: LocalDateTime,
    val size: Int,
    val processed: Boolean,
    val elements: Set<DiagnosticElement>,
    val path: Uri?
) {
    companion object {
        fun Image.asRoomEntity(diagnosisUUID: UUID): ImageRoom =
            ImageRoom(diagnosisUUID, sample, date, size, processed, elements, path)
    }

    fun asApplicationEntity() = Image(sample, date, size, processed, elements, path)
}