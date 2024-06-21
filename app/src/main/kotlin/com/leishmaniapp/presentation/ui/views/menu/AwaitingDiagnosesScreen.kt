package com.leishmaniapp.presentation.ui.views.menu


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
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.presentation.ui.composables.AwaitingDiagnosesTable
import com.leishmaniapp.presentation.ui.composables.AwaitingDiagnosisListTile
import com.leishmaniapp.presentation.ui.layout.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utils.MockGenerator

/**
 * @view A05
 */
@Composable
fun AwaitingDiagnosesScreen(
    specialist: Specialist,
    awaitingDiagnoses: List<com.leishmaniapp.domain.entities.Diagnosis>,
    onDiagnosisClick: (com.leishmaniapp.domain.entities.Diagnosis) -> Unit,
    onSync: () -> Unit,
    onBackButton: () -> Unit
) {
    LeishmaniappScaffold(
        title = stringResource(id = R.string.awaiting_diagnoses),
        backButtonAction = onBackButton
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 0.dp, top = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.specialist),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = specialist.name, style = MaterialTheme.typography.headlineMedium
            )

            AwaitingDiagnosesTable(
                modifier = Modifier
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

            /* Sync Button */
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                IconButton(
                    onClick = onSync,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Text(
                    text = stringResource(id = R.string.synchronize), style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                    ), modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Preview
@Composable
fun AwaitingDiagnosesScreenPreview() {
    LeishmaniappTheme {
        AwaitingDiagnosesScreen(specialist = MockGenerator.mockSpecialist(),
            awaitingDiagnoses = List(4) {
                MockGenerator.mockDiagnosis()
            }, onBackButton = {}, onDiagnosisClick = {}, onSync = {})
    }
}

@Preview
@Composable
fun AwaitingDiagnosesScreenPreview_Empty() {
    LeishmaniappTheme {
        AwaitingDiagnosesScreen(
            specialist = MockGenerator.mockSpecialist(),
            awaitingDiagnoses = listOf(),
            onBackButton = {}, onDiagnosisClick = {}, onSync = {}
        )
    }
}

@Preview
@Composable
fun AwaitingDiagnosesScreenPreview_Overflow() {
    LeishmaniappTheme {
        AwaitingDiagnosesScreen(specialist = MockGenerator.mockSpecialist(),
            awaitingDiagnoses = List(32) { MockGenerator.mockDiagnosis() },
            onBackButton = {},
            onDiagnosisClick = {},
            onSync = {}
        )
    }
}