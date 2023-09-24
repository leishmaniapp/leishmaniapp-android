package com.leishmaniapp.persistance.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.leishmaniapp.entities.DiagnosisModelRoom
import com.leishmaniapp.entities.ModelDiagnosticElementRoom

data class DiagnosticModelWithModelDiagnosticElements(
    @Embedded val diagnosticModel: DiagnosisModelRoom,
    @Relation (
        parentColumn = "id",
        entityColumn = "model"
    )
    val modelDiagnosticElements: ModelDiagnosticElementRoom
)