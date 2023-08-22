package com.leishmaniapp.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.presentation.theme.LeishmaniappTheme

@Composable
fun DiagnosisListTile(modifier: Modifier = Modifier, diagnosis: Diagnosis) {
    ListItem(headlineContent = {
        Text(diagnosis.date.toString())
    }, supportingContent = {
        Text(diagnosis.specialist.name)
    })
}

@Composable
@Preview(showBackground = true)
fun DiagnosisListTilePreview() {
    LeishmaniappTheme {
        DiagnosisListTile(diagnosis = MockGenerator.mockDiagnosis())
    }
}