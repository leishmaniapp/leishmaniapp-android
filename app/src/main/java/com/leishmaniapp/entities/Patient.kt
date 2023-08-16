package com.leishmaniapp.entities

/**
 * Representation of a Patient
 */
data class Patient(
    val name: String,
    val id: String,
    val documentType: DocumentType
)
