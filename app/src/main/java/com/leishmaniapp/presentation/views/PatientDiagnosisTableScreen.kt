package com.leishmaniapp.presentation.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.presentation.theme.LeishmaniappTheme

@Composable
fun PatientDiagnosisTableScreen() {
    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {

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