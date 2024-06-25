package com.leishmaniapp.presentation.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * Show an [AlertDialog] with success informatino
 */
@Composable
fun SuccessAlertDialog(text: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.alert_success))
        },
        text = {
            Text(text = text)
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.accept))
            }
        },
    )
}

@Composable
@Preview
fun SuccessAlertDialogPreview() {
    LeishmaniappTheme {
        SuccessAlertDialog(
            stringResource(id = R.string.alert_inserted_patient)
        ) {}
    }
}
