package com.leishmaniapp.usecases.serialization

import com.leishmaniapp.entities.disease.Disease
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

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