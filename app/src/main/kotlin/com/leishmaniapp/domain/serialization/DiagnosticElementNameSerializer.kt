package com.leishmaniapp.domain.serialization

import com.leishmaniapp.domain.entities.DiagnosticElementName
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serialize [DiagnosticElementName] into its id
 */
object DiagnosticElementNameSerializer : KSerializer<DiagnosticElementName> {

    override val descriptor =
        PrimitiveSerialDescriptor("DiagnosticElementName", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: DiagnosticElementName) =
        encoder.encodeString(value.id)

    override fun deserialize(decoder: Decoder): DiagnosticElementName =
        DiagnosticElementName.diagnosticElementNameById(decoder.decodeString())
            ?: throw SerializationException("DiagnosticElementName does not exist or not supported")
}