package com.leishmaniapp.entities.mock

import androidx.compose.runtime.Composable
import com.leishmaniapp.entities.DiagnosisModel
import com.leishmaniapp.entities.Disease
import kotlinx.serialization.Serializable

@Serializable
object MockDisease : Disease(
    id = "mock.disease", models =
    setOf(
        DiagnosisModel("mock.model_1"),
        DiagnosisModel("mock.model_2"),
    )
)