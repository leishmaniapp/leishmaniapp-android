package com.leishmaniapp.presentation.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun RecoverOngoingDiagnosis(
    onRecover: () -> Unit,
    onDiscard: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = stringResource(id = R.string.alert_recover_diagnosis_title)) },
        text = { Text(text = stringResource(id = R.string.alert_recover_diagnosis_content)) },
        dismissButton = {
            OutlinedButton(onClick = { onDiscard.invoke() }) {
                Text(text = stringResource(id = R.string.discard))
            }
        },
        confirmButton = {
            Button(onClick = { onRecover.invoke() }) {
                Text(text = stringResource(id = R.string.recover))
            }
        },
    )
}

@Composable
@Preview
private fun RecoverOngoingDiagnosisPreview() {
    LeishmaniappTheme {
        RecoverOngoingDiagnosis(
            onDiscard = {},
            onRecover = {}
        )
    }
}