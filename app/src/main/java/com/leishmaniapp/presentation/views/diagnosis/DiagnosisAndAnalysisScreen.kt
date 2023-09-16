package com.leishmaniapp.presentation.views.diagnosis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ModelDiagnosticElement
import com.leishmaniapp.entities.SpecialistDiagnosticElement
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.presentation.ui.DiagnosisActionBar
import com.leishmaniapp.presentation.ui.DiagnosticImageResultsTable
import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import kotlinx.coroutines.launch

internal enum class DiagnosisAndAnalysisPages(
    val title: @Composable () -> Unit,
) {
    ImagePage({ Text(text = stringResource(R.string.tab_image)) }),
    ResultsPage({ Text(text = stringResource(R.string.tab_results)) })
}

@Composable
fun DiagnosisAndAnalysisScreen(
    diagnosis: Diagnosis,
    image: Image,
    onImageChange: (Image) -> Unit
) {

    val pagerState = rememberPagerState(pageCount = { DiagnosisAndAnalysisPages.values().size })

    LeishmaniappScaffold(showHelp = true, bottomBar = {
        DiagnosisActionBar(
            repeatAction = { TODO("On repeat action") },
            analyzeAction = { TODO("On analyze action") },
            nextAction = { TODO("On action next") },
            finishAction = { TODO("On finish action") },
            nextIsCamera = image.processed
        )
    }) {
        Column {
            Row(
                modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = diagnosis.patient.name,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            val coroutineScope = rememberCoroutineScope()
            TabRow(selectedTabIndex = pagerState.currentPage) {
                DiagnosisAndAnalysisPages.values().forEachIndexed { index, item ->
                    Tab(
                        selected = (index == pagerState.currentPage), onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }, text = item.title
                    )
                }
            }

            var editMode by remember {
                mutableStateOf(false)
            }

            HorizontalPager(state = pagerState) { page ->
                when (DiagnosisAndAnalysisPages.values()[page]) {
                    DiagnosisAndAnalysisPages.ImagePage -> {
                        if (editMode) {
                            DiagnosticImageEditSection(image = image) { result, image ->
                                // Get out of Edit Mode
                                editMode = false
                                // Apply changes if image was changed
                                if (result) {
                                    onImageChange.invoke(image)
                                }
                            }
                        } else {
                            DiagnosticImageSection(image = image,
                                onImageEdit = {
                                    editMode = true
                                }, onViewResultsClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(
                                            DiagnosisAndAnalysisPages.ResultsPage.ordinal
                                        )
                                    }
                                })
                        }
                    }

                    DiagnosisAndAnalysisPages.ResultsPage -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            // Show image number
                            Text(
                                text = "%s %d".format(
                                    stringResource(id = R.string.image_number),
                                    image.sample
                                )
                            )

                            // Show results in a table
                            DiagnosticImageResultsTable(
                                modifier = Modifier.padding(vertical = 8.dp),
                                disease = diagnosis.disease,
                                modelDiagnosticElements = image.elements.filterIsInstance<ModelDiagnosticElement>()
                                    .let { if (it.isEmpty()) null else it.toSet() },
                                specialistDiagnosticElements = image.elements.filterIsInstance<SpecialistDiagnosticElement>()
                                    .let { if (it.isEmpty()) null else it.toSet() },
                                onSpecialistEdit = { elementName, specialistDiagnosticElement ->
                                    // Grab the old specialist diagnostic element
                                    image.elements.firstOrNull { diagnosticElement ->
                                        diagnosticElement is SpecialistDiagnosticElement &&
                                                diagnosticElement.name == elementName
                                    }   // If value is not null then apply
                                        ?.let { previousDiagnosticElement ->
                                            // Invoke the image change callback with new modified image
                                            onImageChange.invoke(
                                                image.copy(elements = image.elements.apply {
                                                    // Remove the previous element
                                                    minus(previousDiagnosticElement)
                                                    // Add the new element if not null
                                                    if (specialistDiagnosticElement != null) {
                                                        plus(specialistDiagnosticElement)
                                                    }
                                                })
                                            )
                                        }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun DiagnosisAndAnalysisPreview_NotAnalyzed() {
    LeishmaniappTheme {
        DiagnosisAndAnalysisScreen(
            diagnosis = MockGenerator.mockDiagnosis(),
            image = MockGenerator.mockImage(processed = false)
        ) {

        }
    }
}

@Composable
@Preview
fun DiagnosisAndAnalysisPreview_Analyzed() {
    LeishmaniappTheme {
        DiagnosisAndAnalysisScreen(
            diagnosis = MockGenerator.mockDiagnosis(),
            image = MockGenerator.mockImage(processed = true)
        ) {

        }
    }
}