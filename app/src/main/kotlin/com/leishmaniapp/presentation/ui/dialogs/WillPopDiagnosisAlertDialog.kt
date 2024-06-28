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
fun WillPopDiagnosisAlertDialog(onDismissRequest: () -> Unit, onConfirmExit: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(id = R.string.finish_diagnosis))
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.continue_diagnosis))
            }
        },
        text = {
            Text(text = stringResource(id = R.string.sure_exit_diagnosis))
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmExit,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
            ) {
                Text(text = stringResource(id = R.string.exit))
            }
        },
    )
}

@Preview
@Composable
fun WillPopDiagnosisAlertDialogPreview() {
    LeishmaniappTheme {
        WillPopDiagnosisAlertDialog(onDismissRequest = {}, onConfirmExit = {})
    }
}