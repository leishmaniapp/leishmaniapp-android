package com.leishmaniapp.presentation.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun BackgroundDiagnosisAlertDialog(onDismissRequest: () -> Unit, onConfirmBackground: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        text = {
            Text(text = stringResource(id = R.string.alert_background_diagnosis))
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmBackground,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
            ) {
                Text(text = stringResource(id = R.string.accept))
            }
        },
    )
}

@Preview
@Composable
private fun BackgroundDiagnosisAlertDialogPreview() {
    LeishmaniappTheme {
        BackgroundDiagnosisAlertDialog(onDismissRequest = {}, onConfirmBackground = {})
    }
}