package com.leishmaniapp.domain.entities

import android.os.Parcelable
import com.leishmaniapp.domain.types.Identificator
import com.leishmaniapp.domain.types.ShaHash
import com.leishmaniapp.infrastructure.security.hash
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

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
    val hash: ShaHash by lazy { document.hash() }
}
