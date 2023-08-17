package com.leishmaniapp.entities

import kotlinx.datetime.LocalDateTime
import kotlin.reflect.KClass

abstract class Diagnosis(
    open val result: Boolean,
    open val date: LocalDateTime,
    open val remarks: String,
    open val specialist: Specialist,
    open val patientDiagnosed: Patient,
    open val diagnosticDisease: Disease,
    open val diagnosticImages: MutableSet<Image>,
    open val resultsComputed: Boolean = false,
) {
    val samples: Int
        get() = diagnosticImages.size

    val completed: Boolean
        get() = !diagnosticImages.any { !it.processed }

    fun computeElements(name: String, type: KClass<*>): Int =
        diagnosticImages
            .flatMap { it.diagnosticElements }
            .count { type.isInstance(it) && it.name == name }

    abstract fun computeResults()
}
