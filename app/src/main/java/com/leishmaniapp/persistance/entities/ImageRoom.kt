package com.leishmaniapp.persistance.entities

import android.net.Uri
import androidx.room.Entity
import androidx.room.ForeignKey
import com.leishmaniapp.entities.DiagnosticElement
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ImageAnalysisStatus
import kotlinx.datetime.LocalDateTime
import java.util.UUID

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
    val processed: ImageAnalysisStatus = ImageAnalysisStatus.NotAnalyzed,
    val elements: Set<DiagnosticElement>,
    val path: Uri?
) {
    companion object {
        fun Image.asRoomEntity(diagnosisUUID: UUID): ImageRoom =
            ImageRoom(diagnosisUUID, sample, date, size, processed, elements, path)
    }

    fun asApplicationEntity() = Image(sample, date, size, processed, elements, path)
}