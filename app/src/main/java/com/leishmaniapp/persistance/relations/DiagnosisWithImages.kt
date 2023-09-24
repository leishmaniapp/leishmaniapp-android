package com.leishmaniapp.persistance.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.leishmaniapp.entities.DiagnosisRoom
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ImageRoom

data class DiagnosisWithImages(
    @Embedded val diagnosis: DiagnosisRoom,
    @Relation(
        parentColumn = "id",
        entityColumn = "images"
    )
    val images: List<ImageRoom>
)
