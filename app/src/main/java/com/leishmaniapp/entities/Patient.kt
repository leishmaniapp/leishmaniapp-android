package com.leishmaniapp.entities

import kotlinx.serialization.Serializable

/**
 * Representation of a Patient
 */
@Serializable
data class Patient(
    val name: String,
    val id: IdentificationDocument,
    val documentType: DocumentType
) {
    val documentString: String
        get() = String.format("%s %s", documentType, id)
}
