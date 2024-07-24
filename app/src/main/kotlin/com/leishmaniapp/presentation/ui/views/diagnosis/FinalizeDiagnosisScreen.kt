package com.leishmaniapp.presentation.ui.views.diagnosis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
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
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.presentation.ui.dialogs.FinalizeDiagnosisAlertDialog
import com.leishmaniapp.presentation.ui.layout.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utilities.mock.MockGenerator.mock

/**
 * Finish diagnosis and return [Diagnosis.remarks] and [Diagnosis.Results.specialistResult]
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalizeDiagnosisScreen(
    diagnosis: Diagnosis,
    onGoBack: () -> Unit,
    onDiagnosisFinish: (remarks: String?, specialistResult: Boolean) -> Unit
) {
    var showConfirmationAlert by remember { mutableStateOf(false) }
    var specialistDiagnosisResult by remember { mutableStateOf(false) }
    var specialistRemarks by remember { mutableStateOf("") }

    LeishmaniappScaffold(
        title = stringResource(id = R.string.finalize_diagnosis),
        backButtonAction = onGoBack,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            ) {
                Text(
                    text = stringResource(
                        id = R.string.diagnosis_results_prompt,
                        diagnosis.disease.displayName
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start
                )

                SingleChoiceSegmentedButtonRow {
                    SegmentedButton(
                        selected = !specialistDiagnosisResult,
                        onClick = { specialistDiagnosisResult = false },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = 0,
                            count = 2
                        )
                    ) {
                        Text(text = stringResource(id = R.string.diagnosis_results_negative))
                    }

                    SegmentedButton(
                        selected = specialistDiagnosisResult,
                        onClick = { specialistDiagnosisResult = true },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = 1,
                            count = 2
                        )
                    ) {
                        Text(text = stringResource(id = R.string.diagnosis_results_positive))
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.remarks),
                    style = MaterialTheme.typography.bodyLarge
                )

                TextField(
                    modifier = Modifier.fillMaxSize(),
                    value = specialistRemarks,
                    onValueChange = {
                        specialistRemarks = it
                    })
            }

            Button(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    showConfirmationAlert = true
                }) {
                Text(text = stringResource(id = R.string.accept_and_finish))
            }
        }

        if (showConfirmationAlert) {
            FinalizeDiagnosisAlertDialog(
                onDismissRequest = {
                    showConfirmationAlert = false
                }, onConfirmFinalize = {
                    onDiagnosisFinish.invoke(
                        specialistRemarks.ifBlank { null },
                        specialistDiagnosisResult,
                    )
                })
        }
    }
}

@Composable
@Preview
fun FinalizeDiagnosisScreenPreview() {
    LeishmaniappTheme {
        FinalizeDiagnosisScreen(
            diagnosis = Diagnosis.mock(),
            onGoBack = {},
            onDiagnosisFinish = { _, _ -> },
        )
    }
}
