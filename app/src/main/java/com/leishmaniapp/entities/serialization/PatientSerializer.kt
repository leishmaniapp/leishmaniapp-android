package com.leishmaniapp.entities.serialization

import com.leishmaniapp.entities.Patient
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serialize patient into a Hash
 */
object PatientSerializer : KSerializer<Patient> {
    override val descriptor = PrimitiveSerialDescriptor("Patient", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Patient =
        TODO("Find Patient in memory. is that even possible?")

    override fun serialize(encoder: Encoder, value: Patient) = encoder.encodeString(value.hash)
}