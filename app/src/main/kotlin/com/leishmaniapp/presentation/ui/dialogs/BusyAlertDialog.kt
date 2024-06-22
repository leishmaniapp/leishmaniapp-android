package com.leishmaniapp.presentation.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun BusyAlertDialog() {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {},
        icon = {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(16.dp)
                    .size(32.dp)
            )
        },
        title = {
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(id = R.string.busy),
            )
        },
        text = {
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(id = R.string.busy_content),
                textAlign = TextAlign.Center,
            )
        }
    )
}

@Composable
@Preview
fun BusyAlertDialogPreview() {
    LeishmaniappTheme {
        BusyAlertDialog()
    }
}