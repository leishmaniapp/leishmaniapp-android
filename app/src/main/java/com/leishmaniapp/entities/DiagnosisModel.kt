package com.leishmaniapp.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Diagnostic AI model representation
 */
@Serializable
@JvmInline
@Parcelize
value class DiagnosisModel(val value: String) : Parcelable
