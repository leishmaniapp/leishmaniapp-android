package com.leishmaniapp.persistance.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.leishmaniapp.entities.DiagnosticElementName
import com.leishmaniapp.entities.DiagnosticElementNameRoom
import com.leishmaniapp.entities.disease.DiseaseRoom

data class DiseaseRoomWithDiagnosticElementNames(
    @Embedded val diseaseRoom: DiseaseRoom,
    @Relation(
        parentColumn = "id",
        entityColumn = "elements"
    )
    val diagnosticElementNames: List<DiagnosticElementNameRoom>
)
