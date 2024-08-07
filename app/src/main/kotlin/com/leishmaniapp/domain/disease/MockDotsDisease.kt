package com.leishmaniapp.domain.disease

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.leishmaniapp.R
import com.leishmaniapp.domain.entities.DiagnosticElementName
import com.leishmaniapp.domain.entities.DiagnosticModel
import com.leishmaniapp.domain.entities.ModelDiagnosticElement
import com.leishmaniapp.domain.types.ComputedResultsType

/**
 * Disease that identifies a series of black dots in a white cnavas
 */
data object MockDotsDisease : Disease(
    id = "mock.dots",
    crop = 1000,
    models = setOf(
        DiagnosticModel("mock.dots"),
    ),
    elements = setOf(
        DiagnosticElementName("mock.dots:dot", R.string.mock_dots_disease_element)
    ),
    displayNameResource = R.string.mock_dots_disease,
    painterResource = R.drawable.disease_mock_dots_icon
) {
    /**
     * If there are more than 3 elements, then true
     */
    override fun modelResultForDisease(computedResults: ComputedResultsType): Boolean {
        val results =
            computedResults[MockDotsDisease.elements.first()]
                ?.get(ModelDiagnosticElement::class)
        return (results != null && results > 10)
    }
}