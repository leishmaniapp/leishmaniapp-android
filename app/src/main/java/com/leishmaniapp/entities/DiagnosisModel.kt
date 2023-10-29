package com.leishmaniapp.entities

import android.os.Parcelable
import com.leishmaniapp.usecases.serialization.DiagnosisModelSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Diagnostic AI model representation
 */
@Serializable(with = DiagnosisModelSerializer::class)
@Parcelize
data class DiagnosisModel(val value: String) : Parcelable