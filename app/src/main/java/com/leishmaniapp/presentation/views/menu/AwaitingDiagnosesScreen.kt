package com.leishmaniapp.presentation.views.menu


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
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.presentation.ui.AwaitingDiagnosesTable
import com.leishmaniapp.presentation.ui.AwaitingDiagnosisListTile
import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * @view A05
 */
@Composable
fun AwaitingDiagnosesScreen(
    specialist: Specialist, awaitingDiagnoses: List<Diagnosis>
) {
    LeishmaniappScaffold(
        title = stringResource(id = R.string.awaiting_diagnosis_screen_appbar_title)
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
                        it.items(awaitingDiagnoses) {
                            HorizontalDivider()
                            AwaitingDiagnosisListTile(diagnosis = it) {
                                /* TODO: On diagnosis click */
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
                    onClick = { /* TODO: On sync button press */ },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Text(
                    text = stringResource(id = R.string.label_synchronize), style = TextStyle(
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
            })
    }
}

@Preview
@Composable
fun AwaitingDiagnosesScreenPreview_Empty() {
    LeishmaniappTheme {
        AwaitingDiagnosesScreen(
            specialist = MockGenerator.mockSpecialist(),
            awaitingDiagnoses = listOf()
        )
    }
}

@Preview
@Composable
fun AwaitingDiagnosesScreenPreview_Overflow() {
    LeishmaniappTheme {
        AwaitingDiagnosesScreen(specialist = MockGenerator.mockSpecialist(),
            awaitingDiagnoses = List(32) {
                MockGenerator.mockDiagnosis()
            })
    }
}