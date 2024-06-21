package com.leishmaniapp.infrastructure.persistance.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
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
    @ColumnInfo(name = "token") val token: AccessToken = "",
    @ColumnInfo(name = "diseases") val diseases: Set<Disease> = setOf()
) {
    constructor(specialist: Specialist) : this(
        name = specialist.name,
        email = specialist.email,
        token = specialist.token,
        diseases = specialist.diseases,
    )

    fun toSpecialist(): Specialist = Specialist(
        name = name,
        email = email,
        token = token,
        diseases = diseases
    )
}
