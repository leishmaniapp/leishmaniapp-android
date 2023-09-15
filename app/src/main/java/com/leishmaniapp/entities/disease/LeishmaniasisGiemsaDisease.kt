package com.leishmaniapp.entities.disease

import com.leishmaniapp.entities.DiagnosisModel
import com.leishmaniapp.entities.DiagnosticElementName
import kotlinx.serialization.Serializable

@Serializable(with = LeishmaniasisGiemsaDiseaseSerializer::class)
data object LeishmaniasisGiemsaDisease : Disease(
    id = "leishmaniasis.giemsa",
    models = setOf(
        DiagnosisModel("macrophages"),
        DiagnosisModel("parasites"),
    ),
    elements = setOf(
        DiagnosticElementName("parasite"),
        DiagnosticElementName("macrophage")
    )
)

object LeishmaniasisGiemsaDiseaseSerializer :
    DiseaseSerializer<LeishmaniasisGiemsaDisease>("LeishmaniasisGiemsaDisease")