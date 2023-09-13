package com.leishmaniapp.entities

/**
 * AI model identified elements representation
 */
data class ModelDiagnosticElement(
    override val name: DiagnosticElementName,
    val model: DiagnosisModel,
    val coordinates: Set<Coordinates>
) : DiagnosticElement(name, amount = 0) {
    /**
     * Amount of items depends on list size
     */
    override val amount: Int
        get() = coordinates.size
}