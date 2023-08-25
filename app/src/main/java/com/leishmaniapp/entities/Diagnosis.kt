package com.leishmaniapp.entities

import arrow.core.flatMap
import kotlinx.datetime.LocalDateTime
import kotlin.reflect.KClass

data class Diagnosis(
    val specialistResult: Boolean,
    val modelResult: Boolean,
    val date: LocalDateTime,
    val remarks: String,
    val specialist: Specialist,
    val patientDiagnosed: Patient,
    val diagnosticDisease: Disease,
    val diagnosticImages: MutableSet<Image>,
) {
    var computedResults: Map<String, Map<KClass<out DiagnosticElement>, Int>> = mapOf()

    /**
     * Get the amount of images associated with the diagnosis
     * @TODO Write tests for this function
     */
    val samples: Int
        get() = diagnosticImages.size

    /**
     * Checks if every image in the diagnosis has been processed
     * @TODO Write tests for this function
     */
    val completed: Boolean
        get() = !diagnosticImages.any { !it.processed }

    /**
     * Group [DiagnosticElement] in a map in which the key is a pair of class type
     * (either [ModelDiagnosticElement] or [SpecialistDiagnosticElement], and the element name,
     * and the value is the number of those elements found
     * @TODO Write tests for this function
     */
    fun computeResults() {
        computedResults = diagnosticImages.flatMap {
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
