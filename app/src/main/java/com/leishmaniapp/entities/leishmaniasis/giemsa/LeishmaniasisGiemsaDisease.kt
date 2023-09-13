package com.leishmaniapp.entities.leishmaniasis.giemsa

import com.leishmaniapp.entities.DiagnosisModel
import com.leishmaniapp.entities.DiagnosticElementName
import com.leishmaniapp.entities.Disease
import kotlinx.serialization.Serializable

@Serializable
data object LeishmaniasisGiemsaDisease : Disease(
    id = "leishmaniasis.giemsa",
    models = setOf(
        DiagnosisModel("macrophages"),
        DiagnosisModel("parasites"),
    ),
    diagnosticElements = setOf(
        DiagnosticElementName("parasite"),
        DiagnosticElementName("macrophage")
    )
)