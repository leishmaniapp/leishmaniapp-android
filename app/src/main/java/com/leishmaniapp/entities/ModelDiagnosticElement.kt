package com.leishmaniapp.entities

import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * AI model identified elements representation
 */
@Serializable
@SerialName("model")
@Parcelize
data class ModelDiagnosticElement(
    override val name: DiagnosticElementName,
    val model: DiagnosisModel,
    val coordinates: Set<Coordinates>
) : DiagnosticElement() {
    /**
     * Amount of items depends on list size
     */
    override val amount: Int
        get() = coordinates.size
}