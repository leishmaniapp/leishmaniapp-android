package com.leishmaniapp.persistance.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.leishmaniapp.entities.DiagnosticElementNameRoom
import com.leishmaniapp.entities.DiagnosticElementRoom

data class DiagnosticElementWithDiagnosticElementName (
    @Embedded val diagnosticElement :DiagnosticElementRoom,
    @Relation(
        parentColumn = "id",
        entityColumn = "name"
    )
    val diagnosticElementName: DiagnosticElementNameRoom
)