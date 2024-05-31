package com.leishmaniapp.usecases.serialization

import com.leishmaniapp.entities.DiagnosticModel
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object DiagnosisModelSerializer : KSerializer<DiagnosticModel> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Username", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): DiagnosticModel =
        DiagnosticModel(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: DiagnosticModel) =
        encoder.encodeString(value.value)
}