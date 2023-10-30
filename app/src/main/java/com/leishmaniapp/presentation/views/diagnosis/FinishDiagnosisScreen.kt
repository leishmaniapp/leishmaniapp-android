package com.leishmaniapp.presentation.views.diagnosis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utils.MockGenerator

@Composable
fun FinishDiagnosisScreen(
    diagnosis: Diagnosis, onGoBack: () -> Unit, onDiagnosisFinish: (Diagnosis) -> Unit
) {
    var showAlert by remember {
        mutableStateOf(false)
    }

    var diagnosisResult by remember {
        mutableStateOf(false)
    }

    var remarks by remember {
        mutableStateOf("")
    }

    LeishmaniappScaffold(
        title = stringResource(id = R.string.finish_diagnosis), backButtonAction = onGoBack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 64.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Analysis results
            Column(
                modifier = Modifier.weight(0.25f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.diagnosis_results),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(
                        id = if (diagnosisResult) {
                            R.string.diagnosis_results_positive
                        } else {
                            R.string.diagnosis_results_negative
                        }
                    ), style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center
                )

                Row {
                    Button(modifier = Modifier.padding(8.dp),
                        enabled = diagnosisResult,
                        onClick = { diagnosisResult = false }) {
                        Text(text = stringResource(id = R.string.diagnosis_results_negative))
                    }
                    Button(modifier = Modifier.padding(8.dp),
                        enabled = !diagnosisResult,
                        onClick = { diagnosisResult = true }) {
                        Text(text = stringResource(id = R.string.diagnosis_results_positive))
                    }
                }
            }

            // Remarks
            Column(modifier = Modifier.weight(0.50f)) {
                Text(
                    text = stringResource(id = R.string.remarks),
                    style = MaterialTheme.typography.titleMedium
                )

                TextField(modifier = Modifier.fillMaxSize(), value = remarks, onValueChange = {
                    remarks = it
                })
            }

            Box(
                modifier = Modifier.weight(0.25f), contentAlignment = Alignment.Center
            ) {
                Button(onClick = {
                    showAlert = true
                }) {
                    Text(text = stringResource(id = R.string.accept_and_finish))
                }
            }
        }

        if (showAlert) {
            AlertDialog(onDismissRequest = {
                showAlert = false
            },
                dismissButton = {
                    TextButton(onClick = { showAlert = false }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDiagnosisFinish.invoke(
                                diagnosis.copy(
                                    remarks = remarks, specialistResult = diagnosisResult
                                )
                            )
                        }
                    ) {
                        Text(text = stringResource(id = R.string.accept))
                    }
                },
                text = { Text(text = stringResource(id = R.string.alert_sure_finish_diagnosis)) })
        }
    }
}

@Composable
@Preview
fun FinishDiagnosisScreenPreview() {
    LeishmaniappTheme {
        FinishDiagnosisScreen(diagnosis = MockGenerator.mockDiagnosis(),
            onGoBack = {},
            onDiagnosisFinish = {})
    }
}
