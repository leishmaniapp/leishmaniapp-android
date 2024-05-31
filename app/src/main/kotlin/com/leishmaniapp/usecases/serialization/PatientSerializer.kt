package com.leishmaniapp.usecases.serialization

import com.leishmaniapp.entities.Patient
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object PatientSerializer : KSerializer<Patient> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Patient", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Patient =
        throw SerializationException("Patient cannot be deserialized")

    override fun serialize(encoder: Encoder, value: Patient) = encoder.encodeString(value.hash)
}