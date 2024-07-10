package com.leishmaniapp.domain.disease

import com.leishmaniapp.R
import com.leishmaniapp.domain.entities.DiagnosticElementName
import com.leishmaniapp.domain.entities.DiagnosticModel
import com.leishmaniapp.domain.entities.ModelDiagnosticElement
import com.leishmaniapp.domain.types.ComputedResultsType

/**
 * Leishmaniasis disease with Giemsa tinction.
 * [Leishmaniasis on WHO](https://www.who.int/en/news-room/fact-sheets/detail/leishmaniasis)
 */
data object LeishmaniasisGiemsaDisease : Disease(
    id = "leishmaniasis.giemsa",
    crop = 1944,
    models = setOf(
        DiagnosticModel("leishmaniasis.giemsa.macrophages"),
        DiagnosticModel("leishmaniasis.giemsa.parasites"),
    ),
    elements = setOf(
        DiagnosticElementName(
            "leishmaniasis.giemsa.macrophages:macrophage",
            R.string.leishmaniasis_giemsa_disease_element_macrophages
        ),
        DiagnosticElementName(
            "leishmaniasis.giemsa.parasites:parasite",
            R.string.leishmaniasis_giemsa_disease_element_parasites
        )
    ),
    displayNameResource = R.string.leishmaniasis_giemsa_disease,
    painterResource = R.drawable.disease_leishmaniasis_giemsa_icon
) {
    /**
     * Returns true if any given parasite is found
     */
    override fun modelResultForDisease(computedResults: ComputedResultsType): Boolean {
        val results =
            computedResults[this.elements.first {
                it.id == "leishmaniasis.giemsa:parasite"
            }]?.get(
                ModelDiagnosticElement::class
            )

        return (results != null && results > 0)
    }
}