package com.leishmaniapp.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun DiagnosisResultsTable() {
    LazyColumn {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "Hola")
                }
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "Mundo")
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DiagnosisResultsTablePreview() {
    LeishmaniappTheme {
        DiagnosisResultsTable()
    }
}