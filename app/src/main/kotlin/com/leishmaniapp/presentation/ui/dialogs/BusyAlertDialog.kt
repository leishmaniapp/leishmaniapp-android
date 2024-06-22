package com.leishmaniapp.presentation.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun BusyAlertDialog() {
    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier,
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(id = R.string.busy),
                    textAlign = TextAlign.Center,
                )

                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(32.dp)
                )
            }
        }
    }
}

@Composable
@Preview
fun BusyAlertDialogPreview() {
    LeishmaniappTheme {
        BusyAlertDialog()
    }
}