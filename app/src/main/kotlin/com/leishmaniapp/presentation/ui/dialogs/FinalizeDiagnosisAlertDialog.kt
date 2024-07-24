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
fun FinalizeDiagnosisAlertDialog(onDismissRequest: () -> Unit, onConfirmFinalize: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(id = R.string.finalize_diagnosis))
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        text = {
            Text(text = stringResource(id = R.string.alert_sure_finish_diagnosis))
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmFinalize,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
            ) {
                Text(text = stringResource(id = R.string.finalize_diagnosis))
            }
        },
    )
}

@Preview
@Composable
private fun FinalizeDiagnosisAlertDialogPreview() {
    LeishmaniappTheme {
        FinalizeDiagnosisAlertDialog(onDismissRequest = {}, onConfirmFinalize = {})
    }
}