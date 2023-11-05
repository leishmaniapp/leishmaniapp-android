package com.leishmaniapp.entities.disease

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.leishmaniapp.R
import com.leishmaniapp.entities.ComputedResultsType
import com.leishmaniapp.entities.DiagnosisModel
import com.leishmaniapp.entities.DiagnosticElementName
import com.leishmaniapp.entities.ModelDiagnosticElement
import com.leishmaniapp.usecases.serialization.DiseaseSerializer
import kotlinx.serialization.Serializable

@Serializable(with = LeishmaniasisGiemsaDiseaseSerializer::class)
data object LeishmaniasisGiemsaDisease : Disease(
    id = "leishmaniasis.giemsa",
    models = setOf(
        DiagnosisModel("leishmaniasis.giemsa:macrophages"),
        DiagnosisModel("leishmaniasis.giemsa:parasites"),
    ),
    elements = setOf(
        DiagnosticElementName(
            "leishmaniasis.giemsa:macrophage",
            R.string.leishmaniasis_giemsa_disease_element_macrophages
        ),
        DiagnosticElementName(
            "leishmaniasis.giemsa:parasite",
            R.string.leishmaniasis_giemsa_disease_element_parasites
        )
    )


) {
    override val displayName: String
        @Composable get() = stringResource(id = R.string.leishmaniasis_giemsa_disease)

    override val painterResource: Painter
        @Composable get() = painterResource(id = R.drawable.macrophage)

    /**
     * Returns true if any given parasite is found
     */
    override fun computeDiagnosisResult(computedResults: ComputedResultsType): Boolean {
        val results =
            computedResults[this.elements.first {
                it.value == "leishmaniasis.giemsa:parasite"
            }]?.get(
                ModelDiagnosticElement::class
            )

        return (results != null && results > 0)
    }

}

object LeishmaniasisGiemsaDiseaseSerializer :
    DiseaseSerializer<LeishmaniasisGiemsaDisease>("LeishmaniasisGiemsaDisease")