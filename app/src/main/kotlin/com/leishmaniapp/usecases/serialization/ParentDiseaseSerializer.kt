package com.leishmaniapp.usecases.serialization

import com.leishmaniapp.entities.disease.Disease
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ParentDiseaseSerializer : KSerializer<Disease> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Disease", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Disease) = encoder.encodeString(value.id)
    override fun deserialize(decoder: Decoder): Disease {
        val diseaseId = decoder.decodeString()
        try {
            return Disease::class.sealedSubclasses
                .map { it.objectInstance }
                .first {
                    it?.id == diseaseId
                }!!
        } catch (_: NoSuchElementException) {
            throw SerializationException("Disease does not exist or not supported")
        }
    }
}