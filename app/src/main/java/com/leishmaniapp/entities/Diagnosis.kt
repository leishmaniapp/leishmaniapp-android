package com.leishmaniapp.entities

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID
import kotlin.reflect.KClass

@Serializable
data class Diagnosis(
    val id: UUID = UUID.generateUUID(),
    val specialistResult: Boolean,
    val modelResult: Boolean,
    val date: LocalDateTime,
    val remarks: String,
    val specialist: Specialist,
    val patient: Patient,
    val disease: Disease,
    val images: MutableSet<Image>,
) {
    var computedResults: Map<String, Map<KClass<out DiagnosticElement>, Int>> = mapOf()

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
        get() = !images.any { !it.processed }

    /**
     * Group [DiagnosticElement] in a map in which the key is a pair of class type
     * (either [ModelDiagnosticElement] or [SpecialistDiagnosticElement], and the element name,
     * and the value is the number of those elements found
     * @TODO Write tests for this function
     */
    fun computeResults() {
        computedResults = images.flatMap {
            it.diagnosticElements
        }
            .groupBy { it.name }
            .mapValues {
                it.value.map { diagnosticElement ->
                    Pair(
                        diagnosticElement::class,
                        diagnosticElement.amount
                    )
                }.groupingBy { elementPair ->
                    elementPair.first
                }.aggregate { _, accumulator: Int?, element, _ ->
                    accumulator?.plus(element.second) ?: element.second
                }
            }
    }
}
