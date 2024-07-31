package com.leishmaniapp.domain.entities

import android.os.Parcelable
import androidx.core.net.toFile
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.parceler.LocalDateTimeParceler
import com.leishmaniapp.domain.types.ComputedResultsType
import com.leishmaniapp.utilities.extensions.utcNow
import com.squareup.wire.internal.toUnmodifiableMap
import kotlinx.datetime.LocalDateTime
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import java.io.File
import java.util.UUID

/**
 * Class representing a Diagnosis
 * @immutable Replace by using [Diagnosis.copy]
 */
@Parcelize
data class Diagnosis(

    /**
     * Unique diagnosis identification ID
     */
    val id: UUID = UUID.randomUUID(),

    /**
     * Diagnosis has been deferred for background processing
     */
    val background: Boolean = false,

    /**
     * Diagnosis has been completed and confirmed by specialist
     */
    val finalized: Boolean = false,

    /**
     * UTC time of creation
     */
    @TypeParceler<LocalDateTime, LocalDateTimeParceler> val date: LocalDateTime = LocalDateTime.utcNow(),

    /**
     * Specialist owner of the current diagnosis
     */
    val specialist: Specialist.Record,

    /**
     * Remarks specified by the specialist on completion
     */
    val remarks: String? = null,

    /**
     * Patient whom the diagnosis was issued to
     */
    val patient: Patient,

    /**
     * Disease being diagnosticated
     */
    val disease: Disease,

    /**
     * Group of results computed from detected elements, results computation is a heavy operation
     * and must be called within a coroutine
     */
    val results: Results = Results(),

    /**
     * Image samples associated to this diagnosis with sample name
     */
    @Transient val images: List<ImageSample> = listOf(),

    ) : Parcelable {

    companion object;

    /**
     * Diagnosis total amount of elements results
     */
    @Parcelize
    data class Results(

        /**
         * Positive or Negative diagnosis result given by specialist
         */
        val specialistResult: Boolean,

        /**
         * Diagnostic elements with number of elements found
         */
        val specialistElements: Map<DiagnosticElementName, Int>,

        /**
         * Positive or Negative diagnosis result given by an analysis model
         */
        val modelResult: Boolean,

        /**
         * Diagnostic elements with number of elements found
         */
        val modelElements: Map<DiagnosticElementName, Int>,

        ) : Parcelable {

        companion object;

        /**
         * Create results with everything set to false and empty elements
         */
        constructor() : this(false, mapOf(), false, mapOf())
    }

    /**
     * Group [DiagnosticElement] in a map in which the key is the [DiagnosticElementName] and the
     * value is another map in which the key is the element type (either [ModelDiagnosticElement]
     * or [SpecialistDiagnosticElement], and the value is the number of those elements found.
     * Warning! This is a heavy operation, call it from a coroutine
     */
    @delegate:Transient
    @IgnoredOnParcel
    val computedResults: ComputedResultsType by lazy {
        images.flatMap {
            it.elements
        }.groupBy { it.id }.mapValues {
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
     * Return a new (copy) instance of [Diagnosis] with results set properly
     * using [computedResults] and [Disease.modelResultForDisease]
     */
    fun withResults(specialistResult: Boolean = false): Diagnosis {

        // Variables where the results will be stored
        val specialistElements: MutableMap<DiagnosticElementName, Int> = mutableMapOf()
        val modelElements: MutableMap<DiagnosticElementName, Int> = mutableMapOf()

        // Get the computed results
        computedResults.let { cr ->
            // For each element that has to be diagnosed
            disease.elements.forEach { elementName ->
                // Get the element from computed results or zero if null
                modelElements[elementName] =
                    cr[elementName]?.get(ModelDiagnosticElement::class) ?: 0

                // Get the specialist element from computed results, if specialist
                // didn't provide a value, the same used in model is used
                specialistElements[elementName] =
                    cr[elementName]?.get(SpecialistDiagnosticElement::class)
                        ?: modelElements[elementName]!!
            }
        }

        // Return the copy with the new results
        return copy(
            results = Results(
                specialistResult = specialistResult,
                specialistElements = specialistElements.toUnmodifiableMap(),
                modelResult = disease.modelResultForDisease(computedResults),
                modelElements = modelElements.toUnmodifiableMap(),
            )
        )
    }

    /**
     * Get the amount of images associated with the diagnosis
     */
    val samples: Int
        get() = images.size

    /**
     * Checks if every image in the diagnosis has been processed
     */
    val analyzed: Boolean
        get() = images.all { it.stage == AnalysisStage.Analyzed }

    /**
     * Return a new (copy) instance of [Diagnosis] with the [image] appended to [images]
     */
    fun appendImage(image: ImageSample): Diagnosis =
        copy(images = images.plus(image))

    /**
     * Return a new (copy) instance of [Diagnosis] with the [image] removed from [images]
     */
    fun removeImage(image: ImageSample): Diagnosis =
        copy(images = images.minus(image))

    /**
     * Delete [File] associated to the [ImageSample]
     */
    fun deleteImageFiles() {
        images.forEach { it.file?.toFile()?.delete() }
    }
}
