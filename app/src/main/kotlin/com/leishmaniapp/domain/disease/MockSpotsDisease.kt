package com.leishmaniapp.domain.disease

import com.leishmaniapp.R
import com.leishmaniapp.domain.entities.DiagnosticElementName
import com.leishmaniapp.domain.types.ComputedResultsType

data object MockSpotsDisease : Disease(
    id = "mock.spots",
    crop = 2000,
    elements = setOf(
        DiagnosticElementName("mock.spots:red", R.string.mock_spots_disease_red),
        DiagnosticElementName("mock.spots:green", R.string.mock_spots_disease_green),
        DiagnosticElementName("mock.spots:blue", R.string.mock_spots_disease_blue),
        DiagnosticElementName("mock.spots:cyan", R.string.mock_spots_disease_cyan),
        DiagnosticElementName("mock.spots:yellow", R.string.mock_spots_disease_yellow),
        DiagnosticElementName("mock.spots:magenta", R.string.mock_spots_disease_magenta),
    ),
    displayNameResource = R.string.mock_spots_disease,
    painterResource = R.drawable.disease_mock_spots_icon,
) {
    override fun modelResultForDisease(computedResults: ComputedResultsType): Boolean {
        return computedResults.isNotEmpty()
    }
}