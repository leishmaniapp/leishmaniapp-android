package com.leishmaniapp.domain.entities

import com.leishmaniapp.domain.types.Coordinates
import kotlinx.parcelize.Parcelize

/**
 * AI model identified elements representation
 */
@Parcelize
data class ModelDiagnosticElement(

    override val id: DiagnosticElementName,
    val model: DiagnosticModel,
    val coordinates: Set<Coordinates>

) : DiagnosticElement() {

    /**
     * Amount of items depends on list size
     */
    override val amount: Int
        get() = coordinates.size
}