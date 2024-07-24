package com.leishmaniapp.presentation.ui.views.diagnosis

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
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.presentation.ui.composables.DiagnosisResultsTable
import com.leishmaniapp.presentation.ui.layout.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utilities.mock.MockGenerator.mock

/**
 * @view B04
 */
@Composable
fun DiagnosisTableScreen(
    diagnosis: Diagnosis,
    onBackButton: () -> Unit,
    onShareDiagnosis: () -> Unit
) {
    LeishmaniappScaffold(
        title = stringResource(id = R.string.diagnosis_history_title),
        backButtonAction = onBackButton
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxHeight()
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.align(Alignment.CenterStart)) {
                    Text(
                        text = stringResource(R.string.diagnosis),
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }

                TextButton(modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = { onShareDiagnosis.invoke() }) {
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

            Box(modifier = Modifier.padding(16.dp)) {
                DiagnosisResultsTable(diagnosis = diagnosis)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun DiagnosisTableScreenPreview() {
    LeishmaniappTheme {
        DiagnosisTableScreen(
            diagnosis = Diagnosis.mock(),
            onBackButton = {},
            onShareDiagnosis = {} )
    }
}