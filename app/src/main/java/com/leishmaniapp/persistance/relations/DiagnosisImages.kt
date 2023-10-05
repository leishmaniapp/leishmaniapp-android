package com.leishmaniapp.persistance.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.DiagnosisRoom
import com.leishmaniapp.entities.ImageRoom

class DiagnosisImages(
    @Embedded
    val diagnosis: DiagnosisRoom,
    @Relation(parentColumn = "id", entityColumn = "diagnosisUUID")
    val images: List<ImageRoom>
)