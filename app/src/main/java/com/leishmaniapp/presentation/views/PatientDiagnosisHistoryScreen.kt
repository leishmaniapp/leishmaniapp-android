package com.leishmaniapp.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.presentation.theme.LeishmaniappTheme

/// TODO: Data is mocked, add the corresponding ViewModel
@Composable
fun PatientDiagnosisHistoryScreen(
    diagnosisList: List<Diagnosis> /// TODO: Remove, use viewModel instead
) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = stringResource(R.string.patient_diagnosis_history_screen_appbar_title))
        })
    },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { /*TODO: Start a diagnosis */ }) {
                Icon(
                    Icons.Filled.NoteAdd,
                    contentDescription = stringResource(R.string.start_diagnosis),
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = stringResource(R.string.start_diagnosis))
            }
        }

    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.patient),
                    style = MaterialTheme.typography.bodyLarge
                )
                /// TODO: Provide patient instead
                Text(text = "Camilo Moreno", style = MaterialTheme.typography.headlineMedium)

                // Open diagnosis list
                diagnosisList.apply {
                    if (isNotEmpty()) {
                        LazyColumn(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                            items(this@apply) {
                                diagnosis ->
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
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PatientDiagnosisHistoryScreenPreview() {
    LeishmaniappTheme {
        PatientDiagnosisHistoryScreen(diagnosisList = listOf())
    }
}

@Composable
@Preview(showBackground = true)
fun PatientDiagnosisHistoryScreenPreview_isEmpty() {
    LeishmaniappTheme {
        PatientDiagnosisHistoryScreen(diagnosisList = listOf())
    }
}