package com.leishmaniapp.domain.entities

import com.leishmaniapp.cloud.types.ListOfCoordinates
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.types.BoxCoordinates
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * AI model identified elements representation
 */
@Parcelize
@Serializable
@SerialName("model")
data class ModelDiagnosticElement(

    /**
     * Name of the element
     */
    override val id: DiagnosticElementName,

    /**
     * Version of the identification model
     */
    val version: String,

    /**
     * Identified element coordinates
     */
    val coordinates: Set<BoxCoordinates>

) : DiagnosticElement() {

    companion object {

        /**
         * Create a set of elements from an external response
         */
        fun from(
            x: Map<String, ListOfCoordinates>,
            v: String,
        ): Set<ModelDiagnosticElement> =
            x.map { (key, value) ->
                ModelDiagnosticElement(
                    id = DiagnosticElementName.diagnosticElementNameById(key)!!,

                    version = v,
                    coordinates = value.coordinates.map {
                        BoxCoordinates(
                            x = it.x,
                            y = it.y,
                            w = it.w,
                            h = it.h,
                        )
                    }.toSet()
                )
            }.toSet()

        /**
         * Create a set of elements from an external response without disease key,
         * appends disease.id to the element key (disease:key), if the disease is already present
         * then fallback to the key alone
         */
        fun from(
            x: Map<String, ListOfCoordinates>,
            v: String,
            disease: Disease
        ): Set<ModelDiagnosticElement> =
            x.map { (key, value) ->
                ModelDiagnosticElement(
                    id = DiagnosticElementName.diagnosticElementNameById(
                        // Append the model to the element (model:element)
                        "${disease.id}:$key",
                    ) ?: DiagnosticElementName.diagnosticElementNameById(
                        key,
                    )!!,

                    version = v,
                    coordinates = value.coordinates.map {
                        BoxCoordinates(
                            x = it.x,
                            y = it.y,
                            w = it.w,
                            h = it.h,
                        )
                    }.toSet()
                )
            }.toSet()
    }

    /**
     * Amount of items depends on list size
     */
    override val amount: Int
        get() = coordinates.size
}