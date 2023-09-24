package com.leishmaniapp.persistance.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.leishmaniapp.entities.DiagnosisModel
import com.leishmaniapp.entities.DiagnosisModelRoom
import com.leishmaniapp.entities.disease.DiseaseRoom

data class DiseaseRoomWithDiagnosisModels(
    @Embedded val diseaseRoom: DiseaseRoom,
    @Relation(
        parentColumn = "id",
        entityColumn = "models"
    )
    val diagnosisModels: List<DiagnosisModelRoom>
)
