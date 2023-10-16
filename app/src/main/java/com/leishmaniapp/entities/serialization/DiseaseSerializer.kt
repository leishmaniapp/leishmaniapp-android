package com.leishmaniapp.entities.serialization

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

abstract class DiseaseSerializer<T>(serialName: String) : KSerializer<T>
        where T : Disease {

    override val descriptor = PrimitiveSerialDescriptor(serialName, PrimitiveKind.STRING)

    private val diseaseDelegatedSerializer = ParentDiseaseSerializer
    override fun serialize(encoder: Encoder, value: T) =
        encoder.encodeSerializableValue(diseaseDelegatedSerializer, value as Disease)

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(decoder: Decoder): T =
        decoder.decodeSerializableValue(diseaseDelegatedSerializer) as T
}