package com.leishmaniapp.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.presentation.ui.DiagnosisImageGridItem
import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.RemainingImagesAlert
import com.leishmaniapp.presentation.ui.RemainingImagesProgress
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun DiagnosisImageGridScreen(diagnosis: Diagnosis, isBackground: Boolean) {
    LeishmaniappScaffold(title = stringResource(id = R.string.finish_diagnosis),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = false,
                    enabled = !diagnosis.completed && !isBackground,
                    onClick = { /*TODO*/ },
                    icon = { Icon(Icons.Filled.CameraAlt, contentDescription = null) },
                    label = { Text(text = stringResource(id = R.string.continue_diagnosis)) })
                NavigationBarItem(selected = false,
                    enabled = diagnosis.completed,
                    onClick = { /*TODO*/ },
                    icon = { Icon(Icons.Filled.RemoveRedEye, contentDescription = null) },
                    label = { Text(text = "Ver informe de diagnóstico") })
            }
        }) {
        Column {

            Text(
                text = stringResource(id = R.string.diagnosis_image_grid_screen_header),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp, bottom = 8.dp)
            )

            // Show alerts
            if (!diagnosis.completed) {
                Box(modifier = Modifier.padding(16.dp)) {
                    if (!isBackground) {
                        RemainingImagesAlert()
                    } else {
                        RemainingImagesProgress(
                            done = diagnosis.diagnosticImages.count { image: Image -> image.processed },
                            of = diagnosis.samples
                        )
                    }
                }
            }

            // Get by diagnostic elements types
            val diagnosticElements = diagnosis.diagnosticImages.flatMap { image ->
                image.diagnosticElements
            }.groupBy { diagnosticElement -> diagnosticElement.name }

            // Ordering
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(text = stringResource(id = R.string.order_by))
                Row {
                    FilterChip(modifier = Modifier.padding(4.dp),
                        selected = true,
                        onClick = { /*TODO*/ },
                        label = {
                            Text(text = stringResource(id = R.string.order_by_asc))
                        })

                    FilterChip(modifier = Modifier.padding(4.dp),
                        selected = false,
                        onClick = { /*TODO*/ },
                        label = {
                            Text(text = stringResource(id = R.string.order_by_desc))
                        })
                }
            }

            // Filtering
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(text = stringResource(id = R.string.filter_by))
                LazyRow {
                    item {
                        FilterChip(modifier = Modifier.padding(4.dp),
                            selected = true,
                            onClick = { /*TODO*/ },
                            label = { Text(text = "Todos") })
                    }

                    items(diagnosticElements.keys.toList()) { diagnosticElement ->
                        FilterChip(modifier = Modifier.padding(4.dp),
                            selected = false,
                            onClick = { /*TODO*/ },
                            label = { /*TODO: Use some sort of provider*/
                                Text(text = diagnosticElement)
                            })
                    }
                }
            }


            // Show grid of images
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(diagnosis.diagnosticImages.toList()) { image ->
                    DiagnosisImageGridItem(image = image)
                }
            }
        }
    }
}

@Composable
@Preview
fun DiagnosisImageGridScreenPreview_Done() {
    LeishmaniappTheme {
        DiagnosisImageGridScreen(
            diagnosis = MockGenerator.mockDiagnosis(completed = true), isBackground = false
        )
    }
}

@Composable
@Preview
fun DiagnosisImageGridScreenPreview_Awaiting() {
    LeishmaniappTheme {
        DiagnosisImageGridScreen(
            diagnosis = MockGenerator.mockDiagnosis(), isBackground = false
        )
    }
}

@Composable
@Preview
fun DiagnosisImageGridScreenPreview_AwaitingBackground() {
    LeishmaniappTheme {
        DiagnosisImageGridScreen(
            diagnosis = MockGenerator.mockDiagnosis(), isBackground = true
        )
    }
}