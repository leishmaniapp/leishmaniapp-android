package com.leishmaniapp.persistance.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.leishmaniapp.entities.DiagnosticElementRoom
import com.leishmaniapp.entities.ImageRoom

class ImageWithDiagnosticElement(
    @Embedded val image: ImageRoom,
    @Relation(
        parentColumn = "id",
        entityColumn = "elements"
    )
    val password: List<DiagnosticElementRoom>
)