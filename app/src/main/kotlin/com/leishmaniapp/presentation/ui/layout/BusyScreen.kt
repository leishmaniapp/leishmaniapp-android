package com.leishmaniapp.presentation.ui.layout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.presentation.ui.dialogs.BusyAlertDialog
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * Show a fullscreen [BusyAlertDialog]
 */
@Composable
fun BusyScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        BusyAlertDialog()
    }
}

@Preview
@Composable
private fun BusyScreenPreview() {
    LeishmaniappTheme {
        BusyScreen()
    }
}