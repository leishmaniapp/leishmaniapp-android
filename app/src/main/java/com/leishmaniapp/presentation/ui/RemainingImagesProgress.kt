package com.leishmaniapp.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * Show an alert for remaining images to be processed
 * @param done Number of elements done processing
 * @param of Total number of elements
 * @mockup D03_1
 */
@Composable
fun RemainingImagesProgress(modifier: Modifier = Modifier, done: Int, of: Int) {
    Card(modifier) {
        Box(modifier = Modifier.padding(24.dp)) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Filled.Sync,
                        contentDescription = stringResource(id = R.string.remaining_images_progress_icon),
                        modifier = Modifier
                            .padding(start = 2.dp, end = 12.dp)
                            .size(46.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.remaining_images_progress_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    LinearProgressIndicator(
                        progress = (done.toFloat() / of.toFloat()),
                        trackColor = MaterialTheme.colorScheme.background,
                        strokeCap = StrokeCap.Round
                    )
                    Text(
                        text = String.format("(%d/%d)", done, of),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun RemainingImagesProgressPreview() {
    LeishmaniappTheme {
        RemainingImagesProgress(done = 30, of = 60)
    }
}