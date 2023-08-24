package com.leishmaniapp.presentation.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun PatientDiagnosisTableScreen() {
    LeishmaniappScaffold(title = stringResource(id = R.string.patient_diagnosis_table_screen_appbar_title)) {

    }
}

@Composable
@Preview(showBackground = true)
fun PatientDiagnosisTableScreenPreview() {
    LeishmaniappTheme {
        PatientDiagnosisTableScreen()
    }
}