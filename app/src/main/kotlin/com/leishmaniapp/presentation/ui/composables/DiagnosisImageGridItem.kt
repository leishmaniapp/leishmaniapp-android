package com.leishmaniapp.presentation.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.leishmaniapp.R
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ImageAnalysisStage
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utils.MockGenerator

@Composable
fun DiagnosisImageGridItem(modifier: Modifier = Modifier, image: Image) {
    Card(
        modifier = modifier.width(IntrinsicSize.Max),
    ) {
        AsyncImage(
            model = image.path,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.macrophage)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = String.format("#%d", (image.sample + 1)), modifier = Modifier.padding(8.dp)
            )

            Box(modifier = Modifier.padding(8.dp)) {
                when (image.processed) {
                    ImageAnalysisStage.NotAnalyzed -> Icon(
                        Icons.Filled.Block, contentDescription = stringResource(
                            id = R.string.not_analyzed
                        )
                    )

                    ImageAnalysisStage.Analyzed -> Icon(
                        Icons.Filled.Done,
                        contentDescription = stringResource(id = R.string.processed)
                    )

                    ImageAnalysisStage.Analyzing, ImageAnalysisStage.Deferred -> Icon(
                        Icons.Filled.Sync,
                        contentDescription = stringResource(id = R.string.waiting)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun DiagnosisImageGridItemPreview_Done() {
    LeishmaniappTheme {
        DiagnosisImageGridItem(
            image = MockGenerator.mockImage().copy(processed = ImageAnalysisStage.Analyzed)
        )
    }
}

@Composable
@Preview
fun DiagnosisImageGridItemPreview_Loading() {
    LeishmaniappTheme {
        DiagnosisImageGridItem(
            image = MockGenerator.mockImage().copy(processed = ImageAnalysisStage.Analyzing)
        )
    }
}

@Composable
@Preview
fun DiagnosisImageGridItemPreview_None() {
    LeishmaniappTheme {
        DiagnosisImageGridItem(
            image = MockGenerator.mockImage().copy(processed = ImageAnalysisStage.NotAnalyzed)
        )
    }
}