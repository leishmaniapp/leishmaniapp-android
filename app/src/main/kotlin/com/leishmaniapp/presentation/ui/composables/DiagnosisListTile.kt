package com.leishmaniapp.presentation.ui.composables

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utils.MockGenerator

@Composable
fun DiagnosisListTile(modifier: Modifier = Modifier, diagnosis: com.leishmaniapp.domain.entities.Diagnosis) {
    ListItem(
        modifier = modifier,
        headlineContent = {
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