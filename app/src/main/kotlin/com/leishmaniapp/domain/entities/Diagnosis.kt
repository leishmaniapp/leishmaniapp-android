package com.leishmaniapp.domain.entities

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.entities.protobuf.ToProto
import com.leishmaniapp.utilities.time.toUnixTime
import com.leishmaniapp.utilities.extensions.utcNow
import com.leishmaniapp.domain.entities.protobuf.ProtobufCompatibleEntity
import com.leishmaniapp.domain.parceler.LocalDateTimeParceler
import com.leishmaniapp.domain.types.ComputedResultsType
import com.squareup.wire.internal.toUnmodifiableMap
import kotlinx.datetime.LocalDateTime
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import java.util.UUID

/**
 * Class representing a Diagnosis
 * @immutable Replace by using [Diagnosis.copy]
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Specialist::class,
            childColumns = ["specialist_name", "specialist_email"],
            parentColumns = ["name", "email"]
        ),
        ForeignKey(
            entity = Patient::class,
            childColumns = ["patient_name", "patient_id", "patient_document_type"],
            parentColumns = ["name", "id", "document_type"]
        ),
    ]
)
@Parcelize
data class Diagnosis(

    /**
     * Unique diagnosis identification ID
     */
    @PrimaryKey val id: UUID = UUID.randomUUID(),

    /**
     * Diagnosis has been completed and confirmed by specialist
     */
    @Transient val finalized: Boolean = false,

    /**
     * UTC time of creation
     */
    @TypeParceler<LocalDateTime, LocalDateTimeParceler> val date: LocalDateTime = LocalDateTime.utcNow(),

    /**
     * Specialist owner of the current diagnosis
     */
    @Embedded(prefix = "specialist_") val specialist: Specialist.Record,

    /**
     * Remarks specified by the specialist on completion
     */
    val remarks: String? = null,

    /**
     * Patient whom the diagnosis was issued to
     */
    @Embedded(prefix = "patient_") val patient: Patient,

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
    @Ignore
    @Relation(
        parentColumn = "id",
        entityColumn = "diagnosis"
    )
    val images: List<ImageSample> = listOf(),

    ) : Parcelable, ToProto<com.leishmaniapp.cloud.model.Diagnosis> {

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

        ) : Parcelable,
        ProtobufCompatibleEntity<Results, com.leishmaniapp.cloud.model.Diagnosis.Results> {

        companion object;

        /**
         * Create results with everything set to false and empty elements
         */
        constructor() : this(false, mapOf(), false, mapOf())

        override fun toProto(): com.leishmaniapp.cloud.model.Diagnosis.Results =
            com.leishmaniapp.cloud.model.Diagnosis.Results(specialist_result = specialistResult,
                specialist_elements = specialistElements.mapKeys { entry -> entry.key.id },
                model_result = modelResult,
                model_elements = modelElements.mapKeys { entry -> entry.key.id })

        override fun fromProto(from: com.leishmaniapp.cloud.model.Diagnosis.Results): Results =
            Results(
                specialistResult = from.specialist_result,
                specialistElements = from.specialist_elements.mapKeys { entry ->
                    DiagnosticElementName.diagnosticElementNameById(
                        entry.key
                    )
                },
                modelResult = from.model_result,
                modelElements = from.model_elements.mapKeys { entry ->
                    DiagnosticElementName.diagnosticElementNameById(
                        entry.key
                    )
                },
            )
    }

    /**
     * Group [DiagnosticElement] in a map in which the key is the [DiagnosticElementName] and the
     * value is another map in which the key is the element type (either [ModelDiagnosticElement]
     * or [SpecialistDiagnosticElement], and the value is the number of those elements found.
     * Warning! This is a heavy operation, call it from a coroutine
     */
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

        // Separate grouped results
        computedResults.forEach { (element, type) ->
            specialistElements[element] = type[SpecialistDiagnosticElement::class] ?: 0
            modelElements[element] = type[ModelDiagnosticElement::class] ?: 0
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
     * Return a new (copy) instance of [Diagnosis] with the image added to samples
     */
    fun appendImage(image: ImageSample): Diagnosis =
        this.copy(images = images.plus(image))

    override fun toProto(): com.leishmaniapp.cloud.model.Diagnosis =
        com.leishmaniapp.cloud.model.Diagnosis(
            id = id.toString(),
            disease = disease.id,
            specialist = specialist.toProto(),
            results = results.toProto(),
            date = date.toUnixTime(),
            patient_hash = patient.hash,
            remarks = remarks,
            samples = samples,
        )
}
