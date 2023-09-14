package com.leishmaniapp.entities

import com.leishmaniapp.entities.disease.Disease
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID
import kotlin.reflect.KClass

/**
 * Class representing a Diagnosis
 * @immutable Replace by using [Diagnosis.copy]
 */
@Serializable
data class Diagnosis(
    val id: @Serializable(UUIDSerializer::class) UUID = UUID.randomUUID(),
    val specialistResult: Boolean,
    val modelResult: Boolean,
    val date: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val remarks: String?,
    val specialist: Specialist,
    val patient: Patient,
    val disease: Disease,
    val images: Map<Int, Image>,
) {
    /**
     * Group [DiagnosticElement] in a map in which the key is the [DiagnosticElementName] and the
     * value is another map in which the key is the element type (either [ModelDiagnosticElement]
     * or [SpecialistDiagnosticElement], and the value is the number of those elements found
     * @TODO Write tests for this function
     */
    val computedResults: Map<DiagnosticElementName, Map<KClass<out DiagnosticElement>, Int>> by lazy {
        images.values.flatMap {
            it.elements
        }
            .groupBy { it.name }
            .mapValues {
                it.value.map { diagnosticElement ->
                    diagnosticElement::class to diagnosticElement.amount
                }.groupingBy { elementPair ->
                    elementPair.first
                }.aggregate { _, accumulator: Int?, element, _ ->
                    accumulator?.plus(element.second) ?: element.second
                }
            }
    }

    /**
     * Get the amount of images associated with the diagnosis
     * @TODO Write tests for this function
     */
    val samples: Int
        get() = images.size

    /**
     * Checks if every image in the diagnosis has been processed
     * @TODO Write tests for this function
     */
    val completed: Boolean
        get() = !images.any { !it.value.processed }
}

/**
 * Serializer for the [Diagnosis.id]
 */
object UUIDSerializer : KSerializer<UUID> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): UUID = UUID.fromString(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: UUID) = encoder.encodeString(value.toString())
}
