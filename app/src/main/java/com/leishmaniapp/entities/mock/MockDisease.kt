package com.leishmaniapp.entities.mock

import androidx.compose.runtime.Composable
import com.leishmaniapp.entities.DiagnosisModel
import com.leishmaniapp.entities.Disease

object MockDisease : Disease(id = "mock") {
    override val displayName: String
        @Composable
        get() = "Mocking Disease"

    init {
        models.apply {
            add(DiagnosisModel(model = "mock_1", disease = this@MockDisease))
            add(DiagnosisModel(model = "mock_2", disease = this@MockDisease))
        }
    }
}