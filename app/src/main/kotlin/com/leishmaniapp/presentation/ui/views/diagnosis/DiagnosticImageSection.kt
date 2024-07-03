package com.leishmaniapp.presentation.ui.views.diagnosis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.presentation.ui.composables.DiagnosticImage
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utilities.mock.MockGenerator.mock

/**
 * Show the [DiagnosticImage] alongside a text indicating the [AnalysisStage] or an edit button
 */
@Composable
fun DiagnosticImageSection(
    modifier: Modifier = Modifier,
    image: ImageSample,
    onImageEdit: () -> Unit,
    onViewResultsClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (image.stage) {

                AnalysisStage.Analyzed -> TextButton(
                    modifier = Modifier.padding(8.dp),
                    onClick = onImageEdit
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = stringResource(id = R.string.edit_image),
                        modifier = Modifier.padding(end = 8.dp),
                    )
                    Text(text = stringResource(id = R.string.edit_image))
                }

                AnalysisStage.Enqueued,
                AnalysisStage.NotAnalyzed -> Row(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        Icons.Filled.Error,
                        contentDescription = stringResource(id = R.string.stage_alert_not_analyzed),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(stringResource(id = R.string.stage_alert_not_analyzed))
                }

                AnalysisStage.Analyzing -> Row(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        Icons.Filled.Sync,
                        contentDescription = stringResource(id = R.string.stage_alert_analyzing),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(stringResource(id = R.string.stage_alert_analyzing))
                }

                AnalysisStage.DeliverError, AnalysisStage.Deferred -> Row(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        Icons.Filled.Error,
                        contentDescription = stringResource(id = R.string.stage_alert_deferred),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(stringResource(id = R.string.stage_alert_deferred))
                }

                AnalysisStage.ResultError -> Row(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        Icons.Filled.Error,
                        contentDescription = stringResource(id = R.string.stage_alert_result_error),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(stringResource(id = R.string.stage_alert_result_error))
                }
            }
        }

        DiagnosticImage(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            image = image,
            clickEnabled = false,
            selectedElement = null,
        ) { /* Editing is not enabled */ }

        Button(
            modifier = Modifier.padding(16.dp), onClick = onViewResultsClick
        ) {
            Text(text = stringResource(id = R.string.goto_results))
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun DiagnosticImageSectionPreview_NotProcessed() {
    LeishmaniappTheme {
        DiagnosticImageSection(
            image = ImageSample.mock(stage = AnalysisStage.NotAnalyzed),
            onImageEdit = {}
        ) {}
    }
}

@Composable
@Preview(showBackground = true)
private fun DiagnosticImageSectionPreview_ResultError() {
    LeishmaniappTheme {
        DiagnosticImageSection(
            image = ImageSample.mock(stage = AnalysisStage.ResultError),
            onImageEdit = {}
        ) {}
    }
}

@Composable
@Preview(showBackground = true)
private fun DiagnosticImageSectionPreview_DeliverError() {
    LeishmaniappTheme {
        DiagnosticImageSection(
            image = ImageSample.mock(stage = AnalysisStage.DeliverError),
            onImageEdit = {}
        ) {}
    }
}

@Composable
@Preview(showBackground = true)
private fun DiagnosticImageSectionPreview_Analyzing() {
    LeishmaniappTheme {
        DiagnosticImageSection(
            image = ImageSample.mock(stage = AnalysisStage.Analyzing),
            onImageEdit = {}
        ) {}
    }
}

@Composable
@Preview(showBackground = true)
fun DiagnosticImageSectionPreview_Deferred() {
    LeishmaniappTheme {
        DiagnosticImageSection(
            image = ImageSample.mock(stage = AnalysisStage.Deferred),
            onImageEdit = {}
        ) {}
    }
}

@Composable
@Preview(showBackground = true)
fun DiagnosticImageSectionPreview_Analyzed() {
    LeishmaniappTheme {
        DiagnosticImageSection(
            image = ImageSample.mock(stage = AnalysisStage.Analyzed),
            onImageEdit = {}
        ) {}
    }
}