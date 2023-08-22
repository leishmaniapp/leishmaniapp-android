package com.leishmaniapp.entities.mock

import com.leishmaniapp.entities.DiagnosisModel
import com.leishmaniapp.entities.Disease

object MockDisease : Disease(id = "mock") {
    init {
        models.apply {
            add(DiagnosisModel(model = "model", disease = this@MockDisease))
        }
    }
}