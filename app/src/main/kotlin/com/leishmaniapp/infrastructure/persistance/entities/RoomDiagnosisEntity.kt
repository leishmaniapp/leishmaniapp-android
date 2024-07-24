package com.leishmaniapp.infrastructure.persistance.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.types.Email
import kotlinx.datetime.LocalDateTime
import java.util.UUID

/**
 * SQL representation of a [Diagnosis]
 */
@Entity(
    tableName = "Diagnoses",
    foreignKeys = [
        ForeignKey(
            entity = RoomSpecialistEntity::class,
            parentColumns = ["email"],
            childColumns = ["specialist_email"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = RoomPatientEntity::class,
            childColumns = ["patient_document"],
            parentColumns = ["document"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index(
            value = ["specialist_email"],
            unique = false,
        ),
        Index(
            value = ["patient_document"],
            unique = false,
        ),
    ]
)
data class RoomDiagnosisEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: UUID,
    @ColumnInfo(name = "background") val background: Boolean,
    @ColumnInfo(name = "finalized") val finalized: Boolean,
    @ColumnInfo(name = "date") val date: LocalDateTime,
    @ColumnInfo(name = "specialist_email") val specialistEmail: Email,
    @ColumnInfo(name = "remarks") val remarks: String?,
    @ColumnInfo(name = "patient_document") val patientDocument: String,
    @ColumnInfo(name = "disease") val disease: Disease,
    @Embedded val results: Diagnosis.Results,
) {
    constructor(diagnosis: Diagnosis) : this(
        id = diagnosis.id,
        background = diagnosis.background,
        finalized = diagnosis.finalized,
        date = diagnosis.date,
        specialistEmail = diagnosis.specialist.email,
        remarks = diagnosis.remarks,
        patientDocument = diagnosis.patient.document,
        disease = diagnosis.disease,
        results = diagnosis.results
    )
}