package com.leishmaniapp.usecases.serialization

import com.leishmaniapp.entities.Specialist
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object SpecialistSerializer : KSerializer<Specialist> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Specialist", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Specialist =
        throw SerializationException("Specialist cannot be deserialized")

    override fun serialize(encoder: Encoder, value: Specialist) =
        encoder.encodeString(value.username.value)
}