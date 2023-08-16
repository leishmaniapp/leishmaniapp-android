package com.leishmaniapp.entities.leishmaniasis.giemsa

import com.leishmaniapp.entities.DiagnosisModel
import com.leishmaniapp.entities.Disease

object LeishmaniasisGiemsaDisease : Disease(id = "leishmaniasis.giemsa") {
    init {
        models.apply {
            add(DiagnosisModel(model = "macrophages", disease = this@LeishmaniasisGiemsaDisease))
            add(DiagnosisModel(model = "parasites", disease = this@LeishmaniasisGiemsaDisease))
        }
    }
}