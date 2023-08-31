package com.leishmaniapp.entities.leishmaniasis.giemsa

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.leishmaniapp.R
import com.leishmaniapp.entities.DiagnosisModel
import com.leishmaniapp.entities.Disease
import kotlinx.serialization.Serializable

@Serializable
data object LeishmaniasisGiemsaDisease : Disease(
    id = "leishmaniasis.giemsa",
    models = setOf(
        DiagnosisModel("macrophages"),
        DiagnosisModel("parasites"),
    )
)