package com.leishmaniapp.entities

import androidx.room.Embedded
import androidx.room.Entity
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
@Entity
@Serializable(with = PatientSerializer::class)
data class Patient(
    val name: String,
    @Embedded val id: IdentificationDocument,
    @Embedded val documentType: DocumentType
) {
    /**
     * Get the Patient document with DocumentType and ID concatenated
     */
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

// TODO: Remove
//@Entity
//data class PatientRoom(
//    val name: String,
//    @PrimaryKey(autoGenerate = false)
//    val id: String,
//    val documentType: String
//)

/**
 * Serialize patient into a Hash
 */
object PatientSerializer : KSerializer<Patient> {
    override val descriptor = PrimitiveSerialDescriptor("Patient", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Patient =
        TODO("Find Patient in memory. is that even possible?")

    override fun serialize(encoder: Encoder, value: Patient) = encoder.encodeString(value.hash)
}
