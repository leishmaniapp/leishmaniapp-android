package com.leishmaniapp.usecases.serialization

import com.leishmaniapp.entities.DiagnosticElementName
import com.leishmaniapp.entities.disease.Disease
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serialize into value or deserialize using reflection on sealed class to get all the supported DiagnosticElements
 */
object DiagnosticElementNameSerializer : KSerializer<DiagnosticElementName> {
    override val descriptor =
        PrimitiveSerialDescriptor("DiagnosticElementName", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): DiagnosticElementName {
        val decodedDiagnosticElementName = decoder.decodeString()
        try {
            return Disease::class.sealedSubclasses.flatMap { it.objectInstance!!.elements }
                .first { diagnosticElementName ->
                    diagnosticElementName.value == decodedDiagnosticElementName
                }
        } catch (_: Exception) {
            throw SerializationException("DiagnosticElement(${decodedDiagnosticElementName}) does not exist or is not supported")
        }
    }

    override fun serialize(encoder: Encoder, value: DiagnosticElementName) =
        encoder.encodeString(value.value)
}