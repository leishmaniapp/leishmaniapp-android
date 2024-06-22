package com.leishmaniapp.presentation.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.cloud.types.StatusCode
import com.leishmaniapp.domain.exceptions.LeishmaniappException
import com.leishmaniapp.domain.exceptions.ProcedureExceptionWithStatusCode
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun ErrorAlertDialog(error: LeishmaniappException, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Filled.Error,
                contentDescription = stringResource(id = R.string.exception_title)
            )
        },
        title = {
            Text(text = stringResource(id = R.string.exception_title))
        },
        text = {
            Column {
                Text(
                    text = error.description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Card(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = error.toString(),
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
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
fun ErrorAlertDialogPreview() {
    LeishmaniappTheme {
        ErrorAlertDialog(
            error = ProcedureExceptionWithStatusCode(
                StatusCode.INTERNAL_SERVER_ERROR
            )
        ) {

        }
    }
}