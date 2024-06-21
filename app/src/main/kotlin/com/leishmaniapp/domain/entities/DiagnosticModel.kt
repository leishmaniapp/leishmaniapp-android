package com.leishmaniapp.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Diagnostic AI model representation
 */
@Parcelize
data class DiagnosticModel(val value: String) : Parcelable