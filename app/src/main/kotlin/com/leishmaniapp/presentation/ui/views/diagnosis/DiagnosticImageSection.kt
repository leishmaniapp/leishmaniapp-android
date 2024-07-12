package com.leishmaniapp.presentation.ui.views.diagnosis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.HourglassFull
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.presentation.ui.composables.DiagnosticImage
import com.leishmaniapp.presentation.ui.composables.ImageStageHeader
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

                AnalysisStage.NotAnalyzed -> ImageStageHeader(
                    icon = Icons.Rounded.Image,
                    title = stringResource(id = R.string.stage_not_analyzed_title),
                    content = stringResource(id = R.string.stage_not_analyzed_content)
                )

                AnalysisStage.Enqueued -> ImageStageHeader(
                    icon = Icons.Rounded.HourglassFull,
                    title = stringResource(id = R.string.stage_enqueued_title),
                    content = stringResource(id = R.string.stage_enqueued_content)
                )

                AnalysisStage.Analyzing -> ImageStageHeader(
                    icon = Icons.Rounded.Sync,
                    title = stringResource(id = R.string.stage_analyzing_title),
                    content = stringResource(id = R.string.stage_analyzing_content)
                )

                AnalysisStage.Deferred -> ImageStageHeader(
                    icon = Icons.Rounded.Timer,
                    title = stringResource(id = R.string.stage_deferred_title),
                    content = stringResource(id = R.string.stage_deferred_content)
                )

                AnalysisStage.DeliverError -> ImageStageHeader(
                    icon = Icons.Rounded.Error,
                    title = stringResource(id = R.string.stage_deliver_error_title),
                    content = stringResource(id = R.string.stage_deliver_error_content)
                )

                AnalysisStage.ResultError -> ImageStageHeader(
                    icon = Icons.Rounded.Error,
                    title = stringResource(id = R.string.stage_result_error_title),
                    content = stringResource(id = R.string.stage_result_error_content)
                )

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
private fun DiagnosticImageSectionPreview_NotAnalyzed() {
    LeishmaniappTheme {
        DiagnosticImageSection(
            image = ImageSample.mock(stage = AnalysisStage.NotAnalyzed),
            onImageEdit = {}
        ) {}
    }
}

@Composable
@Preview(showBackground = true)
private fun DiagnosticImageSectionPreview_Enqueued() {
    LeishmaniappTheme {
        DiagnosticImageSection(
            image = ImageSample.mock(stage = AnalysisStage.Enqueued),
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
fun DiagnosticImageSectionPreview_DeliverError() {
    LeishmaniappTheme {
        DiagnosticImageSection(
            image = ImageSample.mock(stage = AnalysisStage.DeliverError),
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
fun DiagnosticImageSectionPreview_Analyzed() {
    LeishmaniappTheme {
        DiagnosticImageSection(
            image = ImageSample.mock(stage = AnalysisStage.Analyzed),
            onImageEdit = {}
        ) {}
    }
}