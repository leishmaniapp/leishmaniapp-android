package com.leishmaniapp.presentation.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.Disease
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.presentation.ui.DataTable
import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun PatientDiagnosisTableScreen() {
    LeishmaniappScaffold(title = stringResource(id = R.string.patient_diagnosis_table_screen_appbar_title)) {
        Column(modifier = Modifier.padding(16.dp)) {

        }
    }
}

@Composable
@Preview(showBackground = true)
fun PatientDiagnosisTableScreenPreview() {
    LeishmaniappTheme {
        PatientDiagnosisTableScreen()
    }
}