package com.leishmaniapp.persistance.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.leishmaniapp.entities.IdentificationDocumentRoom
import com.leishmaniapp.entities.PatientRoom

data class PatientAndIdentificationDocument(
    @Embedded
    val patient: PatientRoom,
    @Relation(
        parentColumn = "documentType",
        entityColumn = "documentType"
    )
    val identificationDocument: IdentificationDocumentRoom
)