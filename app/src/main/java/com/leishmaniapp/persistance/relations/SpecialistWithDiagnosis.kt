package com.leishmaniapp.persistance.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.leishmaniapp.entities.DiagnosisRoom
import com.leishmaniapp.entities.SpecialistRoom

data class SpecialistWithDiagnosis(
    @Embedded val specialist: SpecialistRoom,
    @Relation(
        parentColumn = "id",
        entityColumn = "specialist"
    )
    val Diagnosis: List<DiagnosisRoom>
)