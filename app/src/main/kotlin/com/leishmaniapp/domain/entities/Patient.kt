package com.leishmaniapp.domain.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.leishmaniapp.domain.types.Identificator
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.security.MessageDigest

/**
 * Representation of a Patient
 */
@Entity(primaryKeys = ["id", "document_type"])
@Parcelize
data class Patient(

    val name: String,
    val id: Identificator,
    @ColumnInfo(name = "document_type") val documentType: DocumentType,

    ) : Parcelable {

    /**
     * Get the Patient document with DocumentType and ID concatenated
     */
    @IgnoredOnParcel
    @Ignore
    val document: String = documentType.toString() + id

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
