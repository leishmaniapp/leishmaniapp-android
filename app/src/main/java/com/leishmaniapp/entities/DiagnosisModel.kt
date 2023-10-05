package com.leishmaniapp.entities

import android.os.Parcelable
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

object DiagnosisModelSerializer : KSerializer<DiagnosisModel> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Username", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): DiagnosisModel =
        DiagnosisModel(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: DiagnosisModel) =
        encoder.encodeString(value.value)
}