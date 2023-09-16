package com.leishmaniapp.presentation.views.start


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.entities.disease.Disease
import com.leishmaniapp.entities.disease.LeishmaniasisGiemsaDisease
import com.leishmaniapp.entities.disease.MockDisease
import com.leishmaniapp.presentation.ui.DiseaseItemList
import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * @view A03
 * @TODO Receive list of supported diseases dynamically
 */
@Composable
fun DiseasesMenuScreen(onDiseaseSelection: (Disease) -> Unit) {
    LeishmaniappScaffold {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 0.dp, top = 20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.diseases),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(26.dp))

            var query by remember { mutableStateOf("") }
            var active by remember { mutableStateOf(false) }

            SearchBar(query = query,
                onQueryChange = { query = it },
                onSearch = {
                    active = false/* TODO: On search action */
                },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text(text = stringResource(id = R.string.search_disease)) },
                leadingIcon = {
                    IconButton(onClick = { /* TODO: Prompt search */ }) {
                        Icon(
                            imageVector = Icons.Default.Search, contentDescription = null
                        )
                    }
                }) {}

            Spacer(modifier = Modifier.height(43.dp))

            LazyColumn {
                items(Disease::class.sealedSubclasses.map { it.objectInstance!! }.toList()) { disease ->
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
        DiseasesMenuScreen { _ -> }
    }
}