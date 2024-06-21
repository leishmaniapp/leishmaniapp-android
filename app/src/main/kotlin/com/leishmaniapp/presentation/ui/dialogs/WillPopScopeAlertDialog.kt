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
fun WillPopScopeAlertDialog(onDismissRequest: () -> Unit, onConfirmExit: () -> Unit) {
    AlertDialog(onDismissRequest = onDismissRequest,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmExit,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
            ) {
                Text(text = stringResource(id = R.string.accept))
            }
        }, text = { Text(text = stringResource(id = R.string.sure_exit)) })
}

@Preview
@Composable
fun WillPopScopeAlertDialogPreview() {
    LeishmaniappTheme {
        WillPopScopeAlertDialog(onDismissRequest = {}, onConfirmExit = {})
    }
}