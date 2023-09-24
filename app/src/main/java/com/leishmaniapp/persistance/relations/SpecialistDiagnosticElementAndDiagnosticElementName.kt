package com.leishmaniapp.persistance.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.leishmaniapp.entities.DiagnosticElementNameRoom
import com.leishmaniapp.entities.ModelDiagnosticElementRoom

data class SpecialistDiagnosticElementAndDiagnosticElementName (
    @Embedded
    val specialisrDiagnosticElement: ModelDiagnosticElementRoom,
    @Relation(
        parentColumn = "id",
        entityColumn = "name"
    )
    val diagnosticElementName: DiagnosticElementNameRoom
)