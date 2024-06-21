package com.leishmaniapp.domain.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.entities.protobuf.ProtobufCompatibleEntity
import com.leishmaniapp.domain.types.AccessToken
import com.leishmaniapp.domain.types.Email
import kotlinx.parcelize.Parcelize

/**
 * Specialist representation
 * Algebraic product type with Username and Password which are inlined
 */
@Entity
@Parcelize
data class Specialist(

    val name: String,
    @PrimaryKey val email: Email,
    @Transient val token: AccessToken = "",
    val diseases: Set<Disease> = setOf()

) : Parcelable, ProtobufCompatibleEntity<Specialist, com.leishmaniapp.cloud.model.Specialist> {

    companion object;

    /**
     * [Specialist] with only the [Specialist.email] and [Specialist.name] fields
     */
    @Parcelize
    data class Record(
        val name: String,
        val email: Email,
    ) : Parcelable,
        ProtobufCompatibleEntity<Record, com.leishmaniapp.cloud.model.Specialist.Record> {

        companion object;

        override fun fromProto(from: com.leishmaniapp.cloud.model.Specialist.Record): Record =
            Record(
                email = from.email,
                name = from.name,
            )

        override fun toProto(): com.leishmaniapp.cloud.model.Specialist.Record =
            com.leishmaniapp.cloud.model.Specialist.Record(
                email = email,
                name = name,
            )

    }

    override fun toProto(): com.leishmaniapp.cloud.model.Specialist =
        com.leishmaniapp.cloud.model.Specialist(
            name = this.name,
            email = this.email,
            diseases = this.diseases.map { it.id },
        )

    override fun fromProto(from: com.leishmaniapp.cloud.model.Specialist): Specialist =
        Specialist(
            name = from.name,
            email = from.email,
            diseases = from.diseases.map { Disease.diseaseById(it)!! }.toSet(),
        )
}