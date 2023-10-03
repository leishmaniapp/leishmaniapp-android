package com.leishmaniapp.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.security.MessageDigest

/**
 * Representation of a Patient
 */
@Entity(primaryKeys = ["id", "documentType"])
@Serializable(with = PatientSerializer::class)
data class Patient(
    val name: String,
    val id: IdentificationDocument,
    val documentType: DocumentType
) {
    /**
     * Get the Patient document with DocumentType and ID concatenated
     */
    @Ignore
    val document: String = "%s %s".format(documentType, id)

    /**
     * Calculate the patient hash from its document
     */
    val hash: String by lazy {
        MessageDigest.getInstance("SHA-512")
            .digest(this.document.toByteArray())
            .fold("") { str, byte -> str + "%02x".format(byte) }
    }
}

/**
 * Serialize patient into a Hash
 */
object PatientSerializer : KSerializer<Patient> {
    override val descriptor = PrimitiveSerialDescriptor("Patient", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Patient =
        TODO("Find Patient in memory. is that even possible?")

    override fun serialize(encoder: Encoder, value: Patient) = encoder.encodeString(value.hash)
}
