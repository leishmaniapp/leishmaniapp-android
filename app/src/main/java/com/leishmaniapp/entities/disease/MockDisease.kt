package com.leishmaniapp.entities.disease

import com.leishmaniapp.entities.DiagnosisModel
import com.leishmaniapp.entities.DiagnosticElementName
import kotlinx.serialization.Serializable

@Serializable(with = MockDiseaseSerializer::class)
data object MockDisease : Disease(
    id = "mock.disease",
    models = setOf(
        DiagnosisModel("mock_model"),
    ),
    elements = setOf(
        DiagnosticElementName("mock_element")
    )
)

object MockDiseaseSerializer :
    DiseaseSerializer<MockDisease>("MockDisease")