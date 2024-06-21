package com.leishmaniapp.presentation.ui.composables

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.domain.entities.Patient
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utilities.mock.MockGenerator.mock

@Composable
fun PatientListTile(modifier: Modifier = Modifier, patient: Patient) {
    ListItem(
        modifier = modifier,
        headlineContent = { Text(text = patient.name) },
        supportingContent = { Text(text = patient.document) })
}

@Composable
@Preview
fun PatientListTilePreview() {
    LeishmaniappTheme {
        PatientListTile(patient = Patient.mock())
    }
}