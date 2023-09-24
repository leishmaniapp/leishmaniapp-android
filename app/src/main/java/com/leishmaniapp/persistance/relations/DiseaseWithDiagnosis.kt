package com.leishmaniapp.persistance.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.DiagnosisRoom
import com.leishmaniapp.entities.disease.DiseaseRoom

data class DiseaseWithDiagnosis(
    @Embedded val disease: DiseaseRoom,
    @Relation(
        parentColumn = "id",
        entityColumn = "disease"
    )
    val diagnoses: List<DiagnosisRoom>
)
