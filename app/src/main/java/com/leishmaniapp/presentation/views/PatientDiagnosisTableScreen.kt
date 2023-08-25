package com.leishmaniapp.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachEmail
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.presentation.ui.DiagnosisResultsTable
import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * @view B04
 * TODO: Parameters for view
 */
@Composable
fun PatientDiagnosisTableScreen(diagnosis: Diagnosis) {
    LeishmaniappScaffold(title = stringResource(id = R.string.patient_diagnosis_history_screen_appbar_title),
        backButtonAction = {
            /*TODO: Back button action*/
        }) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.align(Alignment.CenterStart)) {
                    Text(
                        text = stringResource(R.string.diagnosis),
                        style = MaterialTheme.typography.headlineMedium,

                        )
                    Text(
                        text = diagnosis.date.toString(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                TextButton(modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = { /* TODO: Export diagnostic to PDF */ }) {
                    Row {
                        Icon(
                            Icons.Filled.AttachEmail,
                            contentDescription = stringResource(id = R.string.send),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = stringResource(id = R.string.send))
                    }
                }
            }

            Box(modifier = Modifier.padding(32.dp)) {
                DiagnosisResultsTable(diagnosis = diagnosis)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PatientDiagnosisTableScreenPreview() {
    LeishmaniappTheme {
        PatientDiagnosisTableScreen(
            diagnosis = MockGenerator.mockDiagnosis().apply {
                computeResults()
            }
        )
    }
}