package com.leishmaniapp.entities.disease

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.leishmaniapp.R
import com.leishmaniapp.entities.DiagnosisModel
import com.leishmaniapp.entities.DiagnosticElementName
import com.leishmaniapp.usecases.serialization.DiseaseSerializer
import kotlinx.serialization.Serializable

@Serializable(with = MockDiseaseSerializer::class)
data object MockDisease : Disease(
    id = "mock.disease",
    models = setOf(
        DiagnosisModel("mock.disease:mock"),
    ),
    elements = setOf(
        DiagnosticElementName("mock.disease:mock", R.string.mock_disease_element_mock)
    )
) {
    override val displayName: String
        @Composable get() = stringResource(id = R.string.mock_disease)
    override val painterResource: Painter
        @Composable get() = painterResource(id = R.drawable.image_example)
}

object MockDiseaseSerializer :
    DiseaseSerializer<MockDisease>("MockDisease")