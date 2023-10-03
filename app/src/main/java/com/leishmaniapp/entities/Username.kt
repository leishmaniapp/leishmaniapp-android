package com.leishmaniapp.entities

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Functional wrapper class around username
 * Inlined: Doesn't generate runtime overhead
 */
@Serializable(with = UsernameSerializer::class)
data class Username(val value: String)

object UsernameSerializer : KSerializer<Username> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Username", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Username = Username(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: Username) = encoder.encodeString(value.value)
}