package com.leishmaniapp.presentation.ui.views.menu


import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.BuildConfig
import com.leishmaniapp.R
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.disease.LeishmaniasisGiemsaDisease
import com.leishmaniapp.domain.disease.MockDotsDisease
import com.leishmaniapp.presentation.ui.composables.DiseaseItemList
import com.leishmaniapp.presentation.ui.layout.InformationTopBar
import com.leishmaniapp.presentation.ui.layout.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * Show a list with all the available diseases
 * @view A03
 */
@Composable
fun DiseasesMenuScreen(
    diseases: Set<Disease> = Disease.diseases(),
    online: Boolean = false,
    onProfileSelection: () -> Unit,
    onDiseaseSelection: (Disease) -> Unit,
) {
    LeishmaniappScaffold(
        title = stringResource(id = R.string.select_disease),
        actions = {
            IconButton(onClick = onProfileSelection) {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = stringResource(id = R.string.specialist),
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            when (online) {
                true -> InformationTopBar(
                    text = stringResource(
                        id = R.string.online,
                        Uri.parse(BuildConfig.REMOTE_ENDPOINT).host.toString()
                    ),
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.onPrimary
                )

                false -> InformationTopBar(
                    text = stringResource(id = R.string.offline),
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.onSecondary
                )
            }

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
fun DiseasesMenuScreenPreview_Offline() {
    LeishmaniappTheme {
        DiseasesMenuScreen(
            diseases = setOf(
                LeishmaniasisGiemsaDisease, MockDotsDisease
            ),
            online = false,
            onProfileSelection = {},
            onDiseaseSelection = { _ -> },
        )
    }
}

@Preview
@Composable
fun DiseasesMenuScreenPreview_Online() {
    LeishmaniappTheme {
        DiseasesMenuScreen(
            diseases = setOf(
                LeishmaniasisGiemsaDisease, MockDotsDisease
            ),
            onProfileSelection = {},
            onDiseaseSelection = { _ -> },
        )
    }
}