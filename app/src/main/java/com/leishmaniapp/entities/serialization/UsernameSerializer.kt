package com.leishmaniapp.entities.serialization

import com.leishmaniapp.entities.Username
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object UsernameSerializer : KSerializer<Username> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Username", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Username = Username(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: Username) = encoder.encodeString(value.value)
}