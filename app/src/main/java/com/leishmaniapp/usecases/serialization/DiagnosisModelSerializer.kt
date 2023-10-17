package com.leishmaniapp.usecases.serialization

import com.leishmaniapp.entities.DiagnosisModel
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object DiagnosisModelSerializer : KSerializer<DiagnosisModel> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Username", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): DiagnosisModel =
        DiagnosisModel(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: DiagnosisModel) =
        encoder.encodeString(value.value)
}