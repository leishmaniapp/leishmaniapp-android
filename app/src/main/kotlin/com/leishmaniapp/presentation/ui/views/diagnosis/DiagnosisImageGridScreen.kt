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
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ImageAnalysisStage
import com.leishmaniapp.presentation.ui.composables.DiagnosisImageGridItem
import com.leishmaniapp.presentation.ui.layout.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.composables.RemainingImagesAlert
import com.leishmaniapp.presentation.ui.composables.RemainingImagesProgress
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utils.MockGenerator

const val amountOfColumnsInGrid = 3

/**
 * @view D01
 * @view D02
 * @view D03
 */
@Composable
fun DiagnosisImageGridScreen(
    diagnosis: com.leishmaniapp.domain.entities.Diagnosis,
    isBackground: Boolean,
    allowReturn: Boolean = false,
    onBackgroundProcessing: () -> Unit,
    onGoBack: () -> Unit,
    onFinishDiagnosis: () -> Unit,
    onImageClick: (Image) -> Unit
) {
    LeishmaniappScaffold(
        title = stringResource(id = R.string.finish_diagnosis), showHelp = true, bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = false,
                    enabled = allowReturn,
                    onClick = onGoBack,
                    icon = { Icon(Icons.Filled.CameraAlt, contentDescription = null) },
                    label = { Text(text = stringResource(id = R.string.continue_diagnosis)) })
                NavigationBarItem(selected = false,
                    enabled = diagnosis.analyzed,
                    onClick = onFinishDiagnosis,
                    icon = { Icon(Icons.Filled.RemoveRedEye, contentDescription = null) },
                    label = { Text(text = stringResource(id = R.string.continue_to_report)) })
            }
        }, backButtonAction = onGoBack
    ) {

        // Get by diagnostic elements types
        val diagnosticElements = diagnosis.disease.elements

        // Show grid of images
        LazyVerticalGrid(
            columns = GridCells.Fixed(amountOfColumnsInGrid),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(span = { GridItemSpan(amountOfColumnsInGrid) }) {
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
                                    done = diagnosis.images.values.count { image: Image -> image.processed == ImageAnalysisStage.Analyzed },
                                    of = diagnosis.samples
                                )
                            }
                        }
                    }

//                    // Ordering
//                    Text(text = stringResource(id = R.string.order_by))
//                    Row {
//                        FilterChip(modifier = Modifier.padding(4.dp),
//                            selected = true,
//                            onClick = { /*TODO*/ },
//                            label = {
//                                Text(text = stringResource(id = R.string.order_by_asc))
//                            })
//
//                        FilterChip(modifier = Modifier.padding(4.dp),
//                            selected = false,
//                            onClick = { /*TODO*/ },
//                            label = {
//                                Text(text = stringResource(id = R.string.order_by_desc))
//                            })
//                    }
//
//                    // Filtering
//                    Text(text = stringResource(id = R.string.priorize_by))
//                    LazyRow {
//                        items(diagnosticElements.toList()) { diagnosticElement ->
//                            FilterChip(modifier = Modifier.padding(4.dp),
//                                selected = false,
//                                onClick = { /*TODO*/ },
//                                label = {
//                                    Text(text = diagnosticElement.displayName)
//                                })
//                        }
//                    }
                }
            }

            items(diagnosis.images.toList()) { (_, image) ->
                DiagnosisImageGridItem(image = image, modifier = Modifier.clickable {
                    onImageClick.invoke(image)
                })
            }
        }
    }
}

@Composable
@Preview
fun DiagnosisImageGridScreenPreview_Done() {
    LeishmaniappTheme {
        DiagnosisImageGridScreen(diagnosis = MockGenerator.mockDiagnosis(isCompleted = true),
            isBackground = false,
            onBackgroundProcessing = {},
            onGoBack = { },
            onFinishDiagnosis = {},
            onImageClick = {})
    }
}

@Composable
@Preview
fun DiagnosisImageGridScreenPreview_Awaiting() {
    LeishmaniappTheme {
        DiagnosisImageGridScreen(diagnosis = MockGenerator.mockDiagnosis(),
            isBackground = false,
            onBackgroundProcessing = {},
            onGoBack = {},
            onFinishDiagnosis = {},
            onImageClick = {})
    }
}

@Composable
@Preview
fun DiagnosisImageGridScreenPreview_AwaitingBackground() {
    LeishmaniappTheme {
        DiagnosisImageGridScreen(diagnosis = MockGenerator.mockDiagnosis(),
            isBackground = true,
            onBackgroundProcessing = {},
            onGoBack = { },
            onFinishDiagnosis = {},
            onImageClick = {})
    }
}