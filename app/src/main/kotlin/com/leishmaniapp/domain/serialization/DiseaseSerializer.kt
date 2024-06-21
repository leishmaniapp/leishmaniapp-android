package com.leishmaniapp.domain.serialization

import com.leishmaniapp.domain.disease.Disease
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serialize [Disease] into its id
 */
object DiseaseSerializer : KSerializer<Disease> {

    override val descriptor = PrimitiveSerialDescriptor("Disease", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Disease) =
        encoder.encodeString(value.id)

    override fun deserialize(decoder: Decoder): Disease =
        Disease.diseaseById(decoder.decodeString())
            ?: throw SerializationException("Disease does not exist or not supported")
}