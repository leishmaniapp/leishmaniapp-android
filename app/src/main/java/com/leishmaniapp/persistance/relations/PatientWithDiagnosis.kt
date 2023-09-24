package com.leishmaniapp.persistance.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.leishmaniapp.entities.DiagnosisRoom
import com.leishmaniapp.entities.PatientRoom

data class PatientWithDiagnosis(
    @Embedded val patient: PatientRoom,
    @Relation(
        parentColumn = "id",
        entityColumn = "patient"
    )
    val diagnosis: List<DiagnosisRoom>
)