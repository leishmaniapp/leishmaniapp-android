package com.leishmaniapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.Locale

class IdentificationDocument(value: String) {
    @Serializable
    val value: String = value.uppercase(Locale.ROOT)

    override fun toString(): String = value

    override fun equals(other: Any?): Boolean =
        (other is IdentificationDocument) && (other.value == value)

    override fun hashCode(): Int = value.hashCode()
}

@Entity
data class IdentificationDocumentRoom(
    @PrimaryKey(autoGenerate = false)
    val value: String
)