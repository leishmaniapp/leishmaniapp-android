package com.leishmaniapp.persistance.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.leishmaniapp.entities.PasswordRoom
import com.leishmaniapp.entities.SpecialistRoom

data class SpecialistWithPassword (
    @Embedded val specialist: SpecialistRoom,
    @Relation(
        parentColumn = "value",
        entityColumn = "password"
    )
    val password: PasswordRoom
)