package com.leishmaniapp.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import com.leishmaniapp.entities.serialization.PatientSerializer
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
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
@Parcelize
data class Patient(
    val name: String,
    val id: IdentificationDocument,
    val documentType: DocumentType
) : Parcelable {
    /**
     * Get the Patient document with DocumentType and ID concatenated
     */
    @IgnoredOnParcel
    @Ignore
    val document: String = "%s %s".format(documentType, id.value)

    /**
     * Calculate the patient hash from its document
     */
    @IgnoredOnParcel
    val hash: String by lazy {
        MessageDigest.getInstance("SHA-512")
            .digest(this.document.toByteArray())
            .fold("") { str, byte -> str + "%02x".format(byte) }
    }
}
