package com.leishmaniapp.usecases.serialization

import com.leishmaniapp.entities.Email
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object EmailSerializer : KSerializer<Email> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Username", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Email = Email(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: Email) = encoder.encodeString(value.value)
}