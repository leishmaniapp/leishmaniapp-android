package com.leishmaniapp.presentation.ui.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * Show an alert for unprocessed images
 * @mockup D02_2
 */
@Composable
fun RemainingImagesAlert(modifier: Modifier = Modifier, onButtonClick: () -> Unit) {
    Card(modifier) {
        Box(modifier = Modifier.padding(24.dp)) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Filled.Report,
                        contentDescription = stringResource(id = R.string.remaining_images_alert_icon),
                        modifier = Modifier
                            .padding(start = 2.dp, end = 12.dp)
                            .size(46.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.remaining_images_alert_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Text(text = stringResource(id = R.string.remaining_images_alert_content))
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Button(onClick = onButtonClick, modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(id = R.string.remaining_images_alert_btn))
                }
            }
        }
    }
}

@Composable
@Preview
fun RemainingImagesAlertPreview() {
    LeishmaniappTheme {
        RemainingImagesAlert {

        }
    }
}