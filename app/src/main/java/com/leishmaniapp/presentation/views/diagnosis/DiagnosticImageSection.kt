package com.leishmaniapp.presentation.views.diagnosis

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
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ImageAnalysisStatus
import com.leishmaniapp.utils.MockGenerator
import com.leishmaniapp.presentation.ui.DiagnosticImage
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun DiagnosticImageSection(
    modifier: Modifier = Modifier,
    image: Image,
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
            when(image.processed) {
                ImageAnalysisStatus.NotAnalyzed -> Row(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        Icons.Filled.Error,
                        contentDescription = stringResource(id = R.string.alert_not_processed),
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Text(stringResource(id = R.string.alert_not_processed))
                }
                ImageAnalysisStatus.Analyzing -> Row(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        Icons.Filled.Sync,
                        contentDescription = stringResource(id = R.string.alert_being_processed),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(stringResource(id = R.string.alert_being_processed))
                }
                ImageAnalysisStatus.Deferred -> Row(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        Icons.Filled.Error,
                        contentDescription = stringResource(id = R.string.alert_deferred),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(stringResource(id = R.string.alert_deferred))
                }
                ImageAnalysisStatus.Analyzed -> TextButton(modifier = Modifier.padding(8.dp), onClick = onImageEdit) {
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
            selectedElement = null
        ) {}

        Button(
            modifier = Modifier.padding(16.dp), onClick = onViewResultsClick
        ) {
            Text(text = stringResource(id = R.string.goto_results))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DiagnosticImageSectionPreview_NotProcessed() {
    LeishmaniappTheme {
        DiagnosticImageSection(
            image = MockGenerator.mockImage(ImageAnalysisStatus.NotAnalyzed),
            onImageEdit = {}
        ) {}
    }
}

@Composable
@Preview(showBackground = true)
fun DiagnosticImageSectionPreview_Processing() {
    LeishmaniappTheme {
        DiagnosticImageSection(
            image = MockGenerator.mockImage(ImageAnalysisStatus.Analyzing),
            onImageEdit = {}
        ) {}
    }
}

@Composable
@Preview(showBackground = true)
fun DiagnosticImageSectionPreview_Processed() {
    LeishmaniappTheme {
        DiagnosticImageSection(
            image = MockGenerator.mockImage(ImageAnalysisStatus.Analyzed),
            onImageEdit = {}
        ) {}
    }
}

@Composable
@Preview(showBackground = true)
fun DiagnosticImageSectionPreview_Deferred() {
    LeishmaniappTheme {
        DiagnosticImageSection(
            image = MockGenerator.mockImage(ImageAnalysisStatus.Deferred),
            onImageEdit = {}
        ) {}
    }
}
