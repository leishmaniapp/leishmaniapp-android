package com.leishmaniapp.persistance.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.leishmaniapp.entities.SpecialistRoom
import com.leishmaniapp.entities.UsernameRoom

data class SpecialistWithUsername (
    @Embedded val specialist: SpecialistRoom,
    @Relation(
        parentColumn = "value",
        entityColumn = "username"
    )
    val username: UsernameRoom
)