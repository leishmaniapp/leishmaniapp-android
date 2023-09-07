package com.leishmaniapp.entities.mock

import com.leishmaniapp.entities.DiagnosisModel
import com.leishmaniapp.entities.Disease

object MockDisease : Disease(id = "mock.disease") {
    init {
        models.apply {
            add(DiagnosisModel(model = "mock_1", disease = this@MockDisease))
            add(DiagnosisModel(model = "mock_2", disease = this@MockDisease))
        }
    }
}