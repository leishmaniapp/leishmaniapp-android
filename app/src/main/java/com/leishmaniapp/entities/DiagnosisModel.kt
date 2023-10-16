package com.leishmaniapp.entities

import android.os.Parcelable
import com.leishmaniapp.entities.serialization.DiagnosisModelSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Diagnostic AI model representation
 */
@Serializable(with = DiagnosisModelSerializer::class)
@Parcelize
data class DiagnosisModel(val value: String) : Parcelable