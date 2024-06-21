package com.leishmaniapp.domain.entities

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
    val model: DiagnosticModel,
    val coordinates: Set<Coordinates>

) : DiagnosticElement() {

    companion object;

    /**
     * Amount of items depends on list size
     */
    override val amount: Int
        get() = coordinates.size
}