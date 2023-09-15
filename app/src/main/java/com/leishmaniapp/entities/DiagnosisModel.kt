package com.leishmaniapp.entities

import kotlinx.serialization.Serializable

/**
 * Diagnostic AI model representation
 */
@Serializable
@JvmInline
value class DiagnosisModel(val value: String)
