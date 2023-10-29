package com.leishmaniapp.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import com.leishmaniapp.usecases.serialization.PatientSerializer
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.security.MessageDigest

/**
 * Representation of a Patient
 */
@Parcelize
@Serializable(with = PatientSerializer::class)
@Entity(primaryKeys = ["id", "documentType"])
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
