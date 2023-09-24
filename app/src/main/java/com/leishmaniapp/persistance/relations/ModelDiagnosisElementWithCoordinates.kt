package com.leishmaniapp.persistance.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.leishmaniapp.entities.CoordinatesRoom
import com.leishmaniapp.entities.ModelDiagnosticElementRoom

data class ModelDiagnosisElementWithCoordinates (
    @Embedded val modelDiagnosticElement: ModelDiagnosticElementRoom,
    @Relation(
        parentColumn = "id",
        entityColumn = "coordinates"
    )
    val elementWithCoordinates: List<CoordinatesRoom>
)