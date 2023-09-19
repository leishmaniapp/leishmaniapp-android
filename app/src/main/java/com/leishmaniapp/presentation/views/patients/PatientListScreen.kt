package com.leishmaniapp.presentation.views.patients


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.PersonalInjury
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.entities.Patient
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.PatientListTile
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * @view
 */
@Composable
fun PatientListScreen(
    patients: Set<Patient>,
    onAddPatient: () -> Unit,
    onPatientClick: (Patient) -> Unit
) {
    var query: String by remember {
        mutableStateOf("")
    }

    var active by remember { mutableStateOf(false) }

    LeishmaniappScaffold(
        title = stringResource(R.string.patients),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 0.dp, top = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.patient_list),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (patients.isNotEmpty()) {
                Row {
                    SearchBar(
                        modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                        query = query,
                        onQueryChange = { query = it },
                        onSearch = { query = it },
                        active = active,
                        onActiveChange = { active = it },
                        placeholder = { Text(text = stringResource(R.string.patient_search)) },
                        trailingIcon = {
                            IconButton(onClick = onAddPatient) {
                                Icon(
                                    Icons.Filled.AddCircle,
                                    contentDescription = null,
                                )
                            }
                        }
                    ) {
                        LazyColumn {
                            // Get the filtered list of patients
                            val filteredPatients =
                                patients.filter { it.id.value.contains(query) }

                            // Show patients
                            if (filteredPatients.isNotEmpty()) {
                                items(filteredPatients) { patient ->
                                    PatientListTile(modifier = Modifier.clickable {
                                        onPatientClick.invoke(patient)
                                    }, patient = patient)
                                    HorizontalDivider()
                                }
                            } else {
                                item {
                                    Text(
                                        modifier = Modifier.padding(16.dp),
                                        text = stringResource(id = R.string.patient_list_search_no_coincidence),
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn {
                    items(patients.toList()) { patient ->
                        PatientListTile(modifier = Modifier.clickable {
                            onPatientClick.invoke(patient)
                        }, patient = patient)
                        HorizontalDivider()
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .padding(horizontal = 62.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.PersonalInjury,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp),
                        contentDescription = stringResource(
                            R.string.patient_list_empty
                        ),
                    )
                    Text(
                        text = stringResource(R.string.patient_list_empty),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PatientListScreenPreview() {
    LeishmaniappTheme {
        PatientListScreen(patients = buildSet {
            repeat(10) {
                add(MockGenerator.mockPatient())
            }
        }, onAddPatient = {}) {}
    }
}

@Preview
@Composable
fun PatientListScreenPreview_isEmpty() {
    LeishmaniappTheme {
        PatientListScreen(
            patients = setOf(),
            onAddPatient = {},
        ) {}
    }
}