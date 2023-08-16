package com.leishmaniapp.entities

/**
 * AI model identified elements representation
 */
data class ModelDiagnosticElement(
    override val name: String,
    val diagnosisModel: DiagnosisModel,
    val items: List<Pair<Int, Int>>
) : DiagnosticElement(name, amount = 0) {
    /**
     * Amount of items depends on list size
     */
    override val amount: Int
        get() = items.size
}