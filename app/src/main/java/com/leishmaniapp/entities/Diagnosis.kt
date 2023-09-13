package com.leishmaniapp.entities

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID
import kotlin.reflect.KClass

/**
 * Class representing a Diagnosis
 * @immutable Replace by using [Diagnosis.copy]
 */
@Serializable
data class Diagnosis(
    val id: UUID = UUID.generateUUID(),
    val specialistResult: Boolean,
    val modelResult: Boolean,
    val date: LocalDateTime,
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
