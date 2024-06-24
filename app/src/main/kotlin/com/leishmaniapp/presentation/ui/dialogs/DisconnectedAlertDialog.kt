package com.leishmaniapp.presentation.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.SignalWifiConnectedNoInternet4
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun DisconnectedAlertDialog() {
    AlertDialog(
        onDismissRequest = {},
        icon = {
            Icon(
                imageVector = Icons.Filled.SignalWifiConnectedNoInternet4,
                contentDescription = stringResource(id = R.string.exception_title)
            )
        },
        title = {
            Text(text = stringResource(id = R.string.offline))
        },
        text = {
            Text(
                text = stringResource(id = R.string.authentication_offline),
                textAlign = TextAlign.Center,
            )
        },
        confirmButton = {},
    )
}

@Composable
@Preview
fun DisconnectedAlertDialogPreview() {
    LeishmaniappTheme {
        DisconnectedAlertDialog()
    }
}