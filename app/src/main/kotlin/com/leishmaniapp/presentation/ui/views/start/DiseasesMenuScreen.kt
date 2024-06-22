package com.leishmaniapp.presentation.ui.views.start


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.disease.LeishmaniasisGiemsaDisease
import com.leishmaniapp.domain.disease.MockDotsDisease
import com.leishmaniapp.presentation.ui.composables.DiseaseItemList
import com.leishmaniapp.presentation.ui.layout.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * Show a list with all the available diseases
 * @view A03
 */
@Composable
fun DiseasesMenuScreen(
    diseases: Set<Disease> = Disease.diseases(),
    onDiseaseSelection: (Disease) -> Unit,
) {
    LeishmaniappScaffold(
        title = stringResource(id = R.string.select_disease)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 0.dp, top = 20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.diseases),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn {
                items(diseases.toList()) { disease ->
                    DiseaseItemList(disease = disease) {
                        onDiseaseSelection.invoke(disease)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DiseasesMenuScreenPreview() {
    LeishmaniappTheme {
        DiseasesMenuScreen(
            diseases = setOf(
                LeishmaniasisGiemsaDisease,
                MockDotsDisease
            )
        ) { _ -> }
    }
}