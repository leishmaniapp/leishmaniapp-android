package com.leishmaniapp.domain.disease

import com.leishmaniapp.R
import com.leishmaniapp.domain.entities.DiagnosticElementName
import com.leishmaniapp.domain.types.ComputedResultsType

/**
 * Malaria disease with Romanowsky stain.
 * [Malaria on WHO](https://www.who.int/news-room/fact-sheets/detail/malaria)
 */
data object MalariaRomanowskyDisease : Disease(
    id = "malaria.romanowsky",
    crop = 1944,
    elements = setOf(
        DiagnosticElementName(
            "malaria.romanowsky:leukocyte",
            R.string.malaria_romanowsky_disease_element_leukocyte
        ),
        DiagnosticElementName(
            "malaria.romanowsky:schizont",
            R.string.malaria_romanowsky_disease_element_schizont
        ),
        DiagnosticElementName(
            "malaria.romanowsky:trophozoite",
            R.string.malaria_romanowsky_disease_element_trophozoite
        ),
        DiagnosticElementName(
            "malaria.romanowsky:gametocyte",
            R.string.malaria_romanowsky_disease_element_gametocyte
        ),
    ),
    displayNameResource = R.string.malaria_romanowsky_disease,
    painterResource = R.drawable.disease_malaria_romanowsky_icon
) {
    override fun modelResultForDisease(computedResults: ComputedResultsType): Boolean {
        // TODO: Fill in criteria for positive diagnosis
        return true
    }
}