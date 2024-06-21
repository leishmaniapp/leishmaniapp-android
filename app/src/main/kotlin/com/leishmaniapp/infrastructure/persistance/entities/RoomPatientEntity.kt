package com.leishmaniapp.infrastructure.persistance.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.leishmaniapp.domain.entities.DocumentType
import com.leishmaniapp.domain.entities.Patient
import com.leishmaniapp.domain.types.Identificator

/**
 * SQL representation of a [Patient]
 */
@Entity(
    tableName = "Patients",
    indices = [
        Index(
            value = ["id", "document_type"],
            unique = true
        )
    ]
)
data class RoomPatientEntity(
    @PrimaryKey @ColumnInfo(name = "document") val document: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "id") val id: Identificator,
    @ColumnInfo("document_type") val documentType: DocumentType,
) {
    constructor(patient: Patient) : this(
        document = patient.document,
        name = patient.name,
        id = patient.id,
        documentType = patient.documentType,
    )

    fun toPatient(): Patient = Patient(
        name = name,
        id = id,
        documentType = documentType
    )
}
