package com.leishmaniapp.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun AwaitingDiagnosisListTile(diagnosis: Diagnosis, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier,
        headlineContent = {
            Text(
                text = String.format(
                    "%s %s",
                    diagnosis.patient.document,
                    diagnosis.date.toString()
                )
            )
        }, trailingContent = {
            if (diagnosis.completed) {
                Icon(Icons.Filled.Done, contentDescription = null)
            } else {
                Icon(Icons.Filled.Sync, contentDescription = null)
            }
        })
}

@Composable
@Preview
fun AwaitingDiagnosisListTilePreview_Completed() {
    LeishmaniappTheme {
        AwaitingDiagnosisListTile(
            diagnosis = MockGenerator.mockDiagnosis(isCompleted = true)
        ) {
        }
    }
}

@Composable
@Preview
fun AwaitingDiagnosisListTilePreview_Unfinished() {
    LeishmaniappTheme {
        AwaitingDiagnosisListTile(
            diagnosis = MockGenerator.mockDiagnosis(isCompleted = false)
        ) {
        }
    }
}