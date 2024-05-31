package com.leishmaniapp.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leishmaniapp.entities.disease.Disease
import com.leishmaniapp.usecases.serialization.SpecialistSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Specialist representation
 * Algebraic product type with Username and Password which are inlined
 */
@Entity
@Parcelize
@Serializable(with = SpecialistSerializer::class)
data class Specialist(
    val name: String,
    @PrimaryKey val email: Email,
    @Transient val password: Password = Password(""),
    @Transient val diseases: Set<Disease> = setOf()
) : Parcelable {

    /**
     * Transform this specialist into a LeishmaniappCloudServices compatible model defined in [com.leishmaniapp.cloud]
     */
    fun toProto(): com.leishmaniapp.cloud.model.Specialist =
        com.leishmaniapp.cloud.model.Specialist(
            name = this.name,
            email = this.email.value,
            diseases = this.diseases.map { it.id },
        )

    /**
     * Transform this specialist into a LeishmaniappCloudServices compatible model defined in [com.leishmaniapp.cloud]
     */
    fun toProtoRecord(): com.leishmaniapp.cloud.model.Specialist.Record =
        com.leishmaniapp.cloud.model.Specialist.Record(
            name = this.name,
            email = this.email.value,
        )
}