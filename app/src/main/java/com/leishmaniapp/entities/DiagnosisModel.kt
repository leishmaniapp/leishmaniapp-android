package com.leishmaniapp.entities

import kotlinx.serialization.Serializable

/**
 * Diagnostic AI model representation
 */
@Serializable
data class DiagnosisModel(
    val model: String,
    val disease: Disease
)
