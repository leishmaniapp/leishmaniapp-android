package com.leishmaniapp.infrastructure.persistance.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.domain.types.AccessToken
import com.leishmaniapp.domain.types.Email

/**
 * SQL representation of a [Specialist]
 */
@Entity(tableName = "Specialists")
data class RoomSpecialistEntity(
    @PrimaryKey @ColumnInfo(name = "email") val email: Email,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "token") val token: AccessToken? = null,
    @ColumnInfo(name = "diseases") val diseases: Set<Disease> = setOf()
) {
    constructor(specialist: Specialist) : this(
        name = specialist.name,
        email = specialist.email,
        diseases = specialist.diseases,
    )

    fun toSpecialist(): Specialist = Specialist(
        name = name,
        email = email,
        diseases = diseases
    )
}
