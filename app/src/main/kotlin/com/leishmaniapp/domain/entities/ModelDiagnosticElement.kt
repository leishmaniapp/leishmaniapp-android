package com.leishmaniapp.domain.entities

import com.leishmaniapp.cloud.types.ListOfCoordinates
import com.leishmaniapp.domain.types.Coordinates
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

    override val id: DiagnosticElementName,
    val coordinates: Set<Coordinates>

) : DiagnosticElement() {

    companion object {
        fun from(x: Map<String, ListOfCoordinates>): Set<ModelDiagnosticElement> =
            x.map { (key, value) ->
                ModelDiagnosticElement(
                    id = DiagnosticElementName.diagnosticElementNameById(key)!!,
                    coordinates = value.coordinates.map { Coordinates(x = it.x, y = it.y) }.toSet()
                )
            }.toSet()
    }

    /**
     * Amount of items depends on list size
     */
    override val amount: Int
        get() = coordinates.size
}