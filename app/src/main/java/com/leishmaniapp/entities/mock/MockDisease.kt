package com.leishmaniapp.entities.mock

import com.leishmaniapp.entities.DiagnosisModel
import com.leishmaniapp.entities.DiagnosticElementName
import com.leishmaniapp.entities.Disease
import kotlinx.serialization.Serializable

@Serializable
object MockDisease : Disease(
    id = "mock.disease",
    models = setOf(
        DiagnosisModel("mock_model_1"),
        DiagnosisModel("mock_model_2"),
    ),
    diagnosticElements = setOf(
        DiagnosticElementName("mock_element_1"),
        DiagnosticElementName("mock_element_2")
    )
)