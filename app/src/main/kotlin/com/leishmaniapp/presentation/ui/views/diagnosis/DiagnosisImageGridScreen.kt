package com.leishmaniapp.presentation.ui.views.diagnosis

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.presentation.ui.composables.DiagnosisImageGridItem
import com.leishmaniapp.presentation.ui.dialogs.RemainingImagesAlert
import com.leishmaniapp.presentation.ui.dialogs.RemainingImagesProgress
import com.leishmaniapp.presentation.ui.layout.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utilities.mock.MockGenerator.mock

/**
 * @view D02
 * @view D03
 */
@Composable
fun DiagnosisImageGridScreen(
    diagnosis: Diagnosis,
    isBackground: Boolean,
    allowReturn: Boolean = false,
    gridColumns: Int = 3,
    onBackgroundProcessing: () -> Unit,
    onGoBack: () -> Unit,
    onFinalizeDiagnosis: () -> Unit,
    onImageClick: (ImageSample) -> Unit,
) {
    LeishmaniappScaffold(
        title = stringResource(id = R.string.finalize_diagnosis), bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = false,
                    enabled = allowReturn,
                    onClick = onGoBack,
                    icon = { Icon(Icons.Filled.CameraAlt, contentDescription = null) },
                    label = { Text(text = stringResource(id = R.string.continue_diagnosis)) })
                NavigationBarItem(selected = false,
                    enabled = diagnosis.analyzed,
                    onClick = onFinalizeDiagnosis,
                    icon = { Icon(Icons.Filled.RemoveRedEye, contentDescription = null) },
                    label = { Text(text = stringResource(id = R.string.continue_to_report)) })
            }
        }, backButtonAction = onGoBack
    ) { paddingValues ->

        // Show grid of images
        LazyVerticalGrid(
            modifier = Modifier.padding(paddingValues),
            columns = GridCells.Fixed(gridColumns),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(span = { GridItemSpan(gridColumns) }) {
                Column {
                    Text(
                        text = stringResource(id = R.string.diagnosis_image_grid_screen_header),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    // Show alerts
                    if (!diagnosis.analyzed) {

                        var showAlert by remember {
                            mutableStateOf(false)
                        }

                        if (showAlert) {
                            AlertDialog(onDismissRequest = {
                                showAlert = false
                            },
                                dismissButton = {
                                    TextButton(onClick = { showAlert = false }) {
                                        Text(text = stringResource(id = R.string.cancel))
                                    }
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = onBackgroundProcessing
                                    ) {
                                        Text(text = stringResource(id = R.string.accept))
                                    }
                                },
                                text = { Text(text = stringResource(id = R.string.alert_sure_finish_diagnosis)) })
                        }

                        Box(modifier = Modifier.padding(bottom = 12.dp)) {
                            if (!isBackground) {
                                RemainingImagesAlert(onButtonClick = {
                                    showAlert = true
                                })
                            } else {
                                RemainingImagesProgress(
                                    done = diagnosis.images.count { image: ImageSample -> image.stage == AnalysisStage.Analyzed },
                                    of = diagnosis.samples
                                )
                            }
                        }
                    }
                }
            }

            items(diagnosis.images) { image ->
                DiagnosisImageGridItem(image = image, modifier = Modifier.clickable {
                    onImageClick.invoke(image)
                })
            }
        }
    }
}

@Composable
@Preview
private fun DiagnosisImageGridScreenPreview_Done() {
    LeishmaniappTheme {
        DiagnosisImageGridScreen(diagnosis = Diagnosis.mock(isCompleted = true),
            isBackground = false,
            onBackgroundProcessing = {},
            onGoBack = { },
            onFinalizeDiagnosis = {},
            onImageClick = {})
    }
}

@Composable
@Preview
private fun DiagnosisImageGridScreenPreview_Awaiting() {
    LeishmaniappTheme {
        DiagnosisImageGridScreen(diagnosis = Diagnosis.mock(),
            isBackground = false,
            onBackgroundProcessing = {},
            onGoBack = {},
            onFinalizeDiagnosis = {},
            onImageClick = {})
    }
}

@Composable
@Preview
private fun DiagnosisImageGridScreenPreview_AwaitingBackground() {
    LeishmaniappTheme {
        DiagnosisImageGridScreen(diagnosis = Diagnosis.mock(),
            isBackground = true,
            onBackgroundProcessing = {},
            onGoBack = { },
            onFinalizeDiagnosis = {},
            onImageClick = {})
    }
}