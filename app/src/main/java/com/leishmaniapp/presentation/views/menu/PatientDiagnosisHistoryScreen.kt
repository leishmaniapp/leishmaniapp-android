package com.leishmaniapp.presentation.views.menu

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.Patient
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.presentation.ui.DiagnosisListTile
import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * @view B03
 * TODO: Data is mocked, add the corresponding ViewModel
 */
@Composable
fun PatientDiagnosisHistoryScreen(
    patient: Patient,
    diagnosisList: List<Diagnosis> /// TODO: Remove, use viewModel instead
) {
    LeishmaniappScaffold(
        title = stringResource(id = R.string.patient_diagnosis_history_screen_appbar_title),
        backButtonAction = {
            /* TODO: Add navigation */
        }) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 0.dp, top = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.patient),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = patient.name, style = MaterialTheme.typography.headlineMedium
            )

            var searchBarActive by remember {
                mutableStateOf(false)
            }

            var searchQuery by remember {
                mutableStateOf("")
            }

            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = {
                    searchBarActive = false
                    // TODO: Search action
                },
                active = searchBarActive,
                onActiveChange = {
                    searchBarActive = it
                },
                placeholder = {
                    Text(text = stringResource(R.string.search))
                },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = stringResource(R.string.search)
                    )
                }
            ) {
            }

            // Open diagnosis list
            diagnosisList.apply {
                if (isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        items(this@apply) { diagnosis ->
                            DiagnosisListTile(modifier = Modifier.clickable {
                                /*TODO: On diagnosis click*/
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
                                R.string.patient_diagnosis_history_screen_missing
                            ),
                        )
                        Text(
                            text = stringResource(R.string.patient_diagnosis_history_screen_missing),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Button(
            onClick = { /*TODO: Start a diagnosis */ },
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
        PatientDiagnosisHistoryScreen(
            patient = MockGenerator.mockPatient(),
            diagnosisList = List(10) {
                MockGenerator.mockDiagnosis()
            })
    }
}

@Composable
@Preview(showBackground = true)
fun PatientDiagnosisHistoryScreenPreview_isEmpty() {
    LeishmaniappTheme {
        PatientDiagnosisHistoryScreen(
            patient = MockGenerator.mockPatient(),
            diagnosisList = listOf()
        )
    }
}