package com.leishmaniapp.persistance.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.leishmaniapp.entities.DocumentTypeRoom
import com.leishmaniapp.entities.PatientRoom

data class PatientAndDocumentType (
    @Embedded
    val patient: PatientRoom,
    @Relation(
        parentColumn = "value",
        entityColumn = "id"
    )
    val documentType: DocumentTypeRoom
)