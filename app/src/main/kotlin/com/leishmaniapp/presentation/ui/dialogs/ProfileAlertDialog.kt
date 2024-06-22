package com.leishmaniapp.presentation.ui.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.R
import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utilities.mock.MockGenerator.mock

@Composable
fun ProfileAlertDialog(specialist: Specialist, onLogout: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = stringResource(id = R.string.specialist)
            )
        },
        title = {
            Text(text = specialist.name)
        },
        text = {
            Text(text = specialist.email, textAlign = TextAlign.Center)
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.accept))
            }
        },
        confirmButton = {
            TextButton(
                onClick = onLogout,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
            ) {
                Text(text = stringResource(id = R.string.sign_out))
            }
        })
}

@Composable
@Preview
fun ProfileAlertDialogPreview() {
    LeishmaniappTheme {
        ProfileAlertDialog(specialist = Specialist.mock(), onLogout = {}, onDismiss = {})
    }
}