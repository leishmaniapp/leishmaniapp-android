package com.leishmaniapp.persistance.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.DocumentType
import com.leishmaniapp.entities.IdentificationDocument
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.Patient
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.Username
import com.leishmaniapp.entities.disease.Disease
import com.leishmaniapp.usecases.serialization.UUIDSerializer
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Specialist::class,
            childColumns = ["specialistUsername"],
            parentColumns = ["username"]
        ),
        ForeignKey(
            entity = Patient::class,
            childColumns = ["patientIdDocument", "patientIdType"],
            parentColumns = ["id", "documentType"]
        ),
    ]
)

data class DiagnosisRoom(
    @PrimaryKey val id: @Serializable(UUIDSerializer::class) UUID = UUID.randomUUID(),
    val specialistResult: Boolean,
    val modelResult: Boolean,
    val date: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val finalized: Boolean,
    val analyzed: Boolean,
    val remarks: String?,
    val specialistUsername: Username,
    val patientIdDocument: IdentificationDocument,
    val patientIdType: DocumentType,
    val disease: Disease,
) {
    companion object {
        fun Diagnosis.asRoomEntity(): DiagnosisRoom = DiagnosisRoom(
            id,
            specialistResult,
            modelResult,
            date,
            finalized,
            completed,
            remarks,
            specialist.username,
            patient.id,
            patient.documentType,
            disease
        )
    }

    fun asApplicationEntity(
        specialist: Specialist,
        patient: Patient,
        images: List<Image>
    ): Diagnosis =
        Diagnosis(
            id,
            specialistResult,
            modelResult,
            finalized,
            date,
            remarks,
            specialist,
            patient,
            disease,
            images.associateBy { it.sample }
        )
}