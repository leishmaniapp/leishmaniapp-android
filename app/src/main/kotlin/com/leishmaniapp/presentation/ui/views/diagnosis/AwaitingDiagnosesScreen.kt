package com.leishmaniapp.presentation.ui.views.diagnosis


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.presentation.ui.composables.AwaitingDiagnosesTable
import com.leishmaniapp.presentation.ui.composables.AwaitingDiagnosisListTile
import com.leishmaniapp.presentation.ui.layout.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utilities.mock.MockGenerator.mock

/**
 * @view A05
 */
@Composable
fun AwaitingDiagnosesScreen(
    specialist: Specialist,
    awaitingDiagnoses: List<Diagnosis>,
    onDiagnosisClick: (Diagnosis) -> Unit,
    onBackButton: () -> Unit
) {
    LeishmaniappScaffold(
        title = stringResource(id = R.string.awaiting_diagnoses),
        backButtonAction = onBackButton,
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.specialist),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = specialist.name, style = MaterialTheme.typography.headlineMedium
            )

            AwaitingDiagnosesTable(modifier = Modifier
                .padding(vertical = 32.dp)
                .weight(1f),
                contents = if (awaitingDiagnoses.isEmpty()) null else {
                    {
                        it.items(awaitingDiagnoses) { diagnosis ->
                            HorizontalDivider()
                            AwaitingDiagnosisListTile(diagnosis = diagnosis) {
                                onDiagnosisClick.invoke(diagnosis)
                            }
                        }
                    }
                })
        }
    }
}

@Preview
@Composable
private fun AwaitingDiagnosesScreenPreview() {
    LeishmaniappTheme {
        AwaitingDiagnosesScreen(
            specialist = Specialist.mock(),
            awaitingDiagnoses = List(4) {
                Diagnosis.mock()
            },
            onBackButton = {},
            onDiagnosisClick = {},
        )
    }
}

@Preview
@Composable
private fun AwaitingDiagnosesScreenPreview_Empty() {
    LeishmaniappTheme {
        AwaitingDiagnosesScreen(
            specialist = Specialist.mock(),
            awaitingDiagnoses = listOf(),
            onBackButton = {},
            onDiagnosisClick = {},
        )
    }
}

@Preview
@Composable
private fun AwaitingDiagnosesScreenPreview_Overflow() {
    LeishmaniappTheme {
        AwaitingDiagnosesScreen(
            specialist = Specialist.mock(),
            awaitingDiagnoses = List(32) { Diagnosis.mock() },
            onBackButton = {},
            onDiagnosisClick = {},
        )
    }
}