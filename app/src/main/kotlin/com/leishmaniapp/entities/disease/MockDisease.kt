package com.leishmaniapp.entities.disease

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.leishmaniapp.R
import com.leishmaniapp.entities.ComputedResultsType
import com.leishmaniapp.entities.DiagnosticModel
import com.leishmaniapp.entities.DiagnosticElementName
import com.leishmaniapp.entities.ModelDiagnosticElement
import com.leishmaniapp.usecases.serialization.DiseaseSerializer
import kotlinx.serialization.Serializable

@Serializable(with = MockDiseaseSerializer::class)
data object MockDisease : Disease(
    id = "mock.disease",
    models = setOf(
        DiagnosticModel("mock.disease:mock"),
    ),
    elements = setOf(
        DiagnosticElementName("mock.disease:mock", R.string.mock_disease_element_mock)
    )
) {
    override val displayName: String
        @Composable get() = stringResource(id = R.string.mock_disease)

    override val painterResource: Painter
        @Composable get() = painterResource(id = R.drawable.macrophage)

    /**
     * If there are more than 10 elements, then true
     */
    override fun computeDiagnosisResult(computedResults: ComputedResultsType): Boolean {
        val results =
            computedResults[MockDisease.elements.first()]
                ?.get(ModelDiagnosticElement::class)
        return (results != null && results > 10)
    }
}

object MockDiseaseSerializer :
    DiseaseSerializer<MockDisease>("MockDisease")