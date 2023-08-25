package com.leishmaniapp.entities.leishmaniasis.giemsa

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.leishmaniapp.R
import com.leishmaniapp.entities.DiagnosisModel
import com.leishmaniapp.entities.Disease

data object LeishmaniasisGiemsaDisease : Disease(
    id = "leishmaniasis.giemsa"
) {
    override val displayName: String
        @Composable
        get() = stringResource(id = R.string.leishmaniasis_giemsa_disease_name)

    init {
        models.apply {
            add(DiagnosisModel(model = "macrophages", disease = this@LeishmaniasisGiemsaDisease))
            add(DiagnosisModel(model = "parasites", disease = this@LeishmaniasisGiemsaDisease))
        }
    }
}