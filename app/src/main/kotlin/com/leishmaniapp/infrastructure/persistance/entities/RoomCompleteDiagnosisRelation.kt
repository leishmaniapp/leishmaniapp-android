package com.leishmaniapp.infrastructure.persistance.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.utilities.extensions.toRecord

/**
 * [RoomDiagnosisEntity] with complete [RoomSpecialistEntity] and [RoomImageEntity] data,
 * this is the base relationship for building a [Diagnosis]
 */
data class RoomCompleteDiagnosisRelation(
    @Embedded val diagnosis: RoomDiagnosisEntity,

    @Relation(
        parentColumn = "specialist_email",
        entityColumn = "email"
    )
    val specialist: RoomSpecialistEntity,

    @Relation(
        parentColumn = "patient_document",
        entityColumn = "document"
    )
    val patient: RoomPatientEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "diagnosis"
    )
    val images: List<RoomImageEntity>,
) {
    fun toDiagnosis(): Diagnosis = Diagnosis(
        id = diagnosis.id,
        background = diagnosis.background,
        finalized = diagnosis.finalized,
        date = diagnosis.date,
        specialist = specialist.toSpecialist().toRecord(),
        remarks = diagnosis.remarks,
        patient = patient.toPatient(),
        disease = diagnosis.disease,
        results = diagnosis.results,
        images = images.map { it.toImageSample() },
    )
}