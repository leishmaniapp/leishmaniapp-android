package com.leishmaniapp.domain.entities

import android.os.Parcelable
import com.leishmaniapp.domain.types.Identificator
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.security.MessageDigest

/**
 * Representation of a Patient
 */
@Parcelize
data class Patient(

    val name: String,
    val id: Identificator,
    val documentType: DocumentType,

    ) : Parcelable {

    companion object;

    /**
     * Get the Patient document with DocumentType and ID concatenated by a point
     */
    @IgnoredOnParcel
    val document: String = "$documentType.$id"

    /**
     * Calculate the patient hash from its document
     */
    @IgnoredOnParcel
    val hash: String by lazy {
        MessageDigest.getInstance("SHA-512")
            .digest(document.toByteArray())
            .fold("") { str, byte -> str + "%02x".format(byte) }
    }
}
