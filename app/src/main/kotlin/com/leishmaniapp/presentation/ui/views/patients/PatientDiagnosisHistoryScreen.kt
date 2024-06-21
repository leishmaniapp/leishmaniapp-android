package com.leishmaniapp.presentation.ui.views.patients

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.leishmaniapp.presentation.ui.composables.DiagnosisListTile
import com.leishmaniapp.presentation.ui.layout.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utils.MockGenerator

/**
 * @view B03
 */
@Composable
fun PatientDiagnosisHistoryScreen(
    patient: Patient,
    diagnosisList: List<com.leishmaniapp.domain.entities.Diagnosis>,
    onBackButton: (() -> Unit)? = null,
    onDiagnosisClick: (com.leishmaniapp.domain.entities.Diagnosis) -> Unit,
    onDiagnosisCreate: () -> Unit,
) {
    var searchBarActive by remember {
        mutableStateOf(false)
    }

    var query: String by remember {
        mutableStateOf("")
    }

    LeishmaniappScaffold(
        title = stringResource(id = R.string.diagnosis_history_title),
        backButtonAction = onBackButton
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 0.dp, top = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.patient), style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = patient.name, style = MaterialTheme.typography.headlineMedium
            )

            if (diagnosisList.isNotEmpty()) {

                SearchBar(modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = { query = it },
                    active = searchBarActive,
                    onActiveChange = { searchBarActive = it },
                    placeholder = { Text(text = stringResource(R.string.search)) },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = stringResource(R.string.search)
                        )
                    }) {
                    LazyColumn {
                        // Get the filtered list of patients
                        val filteredDiagnosis =
                            diagnosisList.filter { it.date.toString().contains(query) }

                        // Show patients
                        if (filteredDiagnosis.isNotEmpty()) {
                            items(filteredDiagnosis) { diagnosis ->
                                DiagnosisListTile(modifier = Modifier.clickable {
                                    onDiagnosisClick.invoke(diagnosis)
                                }, diagnosis)
                                HorizontalDivider()
                            }
                        } else {
                            item {
                                Text(
                                    modifier = Modifier.padding(16.dp),
                                    text = stringResource(id = R.string.diagnosis_list_search_no_coincidence),
                                )
                            }
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    items(diagnosisList) { diagnosis ->
                        DiagnosisListTile(modifier = Modifier.clickable {
                            onDiagnosisClick.invoke(diagnosis)
                        }, diagnosis)
                        HorizontalDivider()
                    }

                    item {
                        // Bottom List padding
                        Box(modifier = Modifier.height(100.dp))
                    }
                }
            } else /* There are no element available */ {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .padding(horizontal = 62.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.AssignmentLate,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp),
                        contentDescription = stringResource(
                            R.string.no_previous_diagnosis
                        ),
                    )
                    Text(
                        text = stringResource(R.string.no_previous_diagnosis),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Button(
            onClick = onDiagnosisCreate,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text(text = stringResource(R.string.start_diagnosis))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PatientDiagnosisHistoryScreenPreview() {
    LeishmaniappTheme {
        PatientDiagnosisHistoryScreen(patient = MockGenerator.mockPatient(),
            diagnosisList = List(10) {
                MockGenerator.mockDiagnosis()
            },
            onDiagnosisClick = {},
            onDiagnosisCreate = {})
    }
}

@Composable
@Preview(showBackground = true)
fun PatientDiagnosisHistoryScreenPreview_isEmpty() {
    LeishmaniappTheme {
        PatientDiagnosisHistoryScreen(patient = MockGenerator.mockPatient(),
            diagnosisList = listOf(),
            onDiagnosisClick = {},
            onDiagnosisCreate = {})
    }
}