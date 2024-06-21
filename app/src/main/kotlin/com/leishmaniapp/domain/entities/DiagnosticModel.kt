package com.leishmaniapp.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Diagnostic AI model representation
 */
@Parcelize
@Serializable
data class DiagnosticModel(val value: String) : Parcelable