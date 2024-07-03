package com.leishmaniapp.presentation.ui.views.diagnosis

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.entities.ModelDiagnosticElement
import com.leishmaniapp.domain.entities.SpecialistDiagnosticElement
import com.leishmaniapp.presentation.ui.composables.DiagnosticImageResultsTable
import com.leishmaniapp.presentation.ui.layout.DiagnosisActionBar
import com.leishmaniapp.presentation.ui.layout.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utilities.mock.MockGenerator.mock
import kotlinx.coroutines.launch

/**
 * Pages to show within the [DiagnosisAndAnalysisScreen]
 */
private enum class DiagnosisAndAnalysisPages(
    val title: @Composable () -> Unit,
) {
    IMAGE_PAGE({ Text(text = stringResource(R.string.tab_image)) }),
    RESULTS_PAGE({ Text(text = stringResource(R.string.tab_results)) })
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun DiagnosisAndAnalysisScreen(
    diagnosis: Diagnosis,
    image: ImageSample,
    onImageChange: (ImageSample) -> Unit,
    analysisInProgress: Boolean = false,
    onRepeatAction: () -> Unit,
    onAnalyzeAction: () -> Unit,
    onNextAction: () -> Unit,
    onFinishAction: () -> Unit,
    onNextActionNotAnalyzed: (() -> Unit)? = null
) {

    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { DiagnosisAndAnalysisPages.entries.size })
    var editMode by remember { mutableStateOf(false) }

    LeishmaniappScaffold(bottomBar = {
        DiagnosisActionBar(
            repeatAction = onRepeatAction,
            analyzeAction = onAnalyzeAction,
            analysisStage = image.stage,
            finishAction = onFinishAction,
            analysisShowAsLoading = analysisInProgress,
            nextAction = {
                when (image.stage) {
                    AnalysisStage.NotAnalyzed, AnalysisStage.Analyzing ->
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                DiagnosisAndAnalysisPages.RESULTS_PAGE.ordinal
                            )
                            onNextActionNotAnalyzed?.invoke()
                        }

                    AnalysisStage.ResultError,
                    AnalysisStage.DeliverError,
                    AnalysisStage.Deferred,
                    AnalysisStage.Enqueued,
                    AnalysisStage.Analyzed -> onNextAction.invoke()
                }
            },
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            // Show the patient name
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

            // Show the tab selection
            TabRow(selectedTabIndex = pagerState.currentPage) {
                DiagnosisAndAnalysisPages.entries.forEachIndexed { index, item ->
                    Tab(
                        selected = (index == pagerState.currentPage), onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }, text = item.title
                    )
                }
            }

            // Select the current page
            HorizontalPager(state = pagerState) { page ->
                when (DiagnosisAndAnalysisPages.entries[page]) {
                    DiagnosisAndAnalysisPages.IMAGE_PAGE -> {
                        if (editMode) {
                            DiagnosticImageEditSection(
                                image = image,
                                onImageChange = { image ->
                                    // Change the image
                                    onImageChange.invoke(image)
                                    // Get out of Edit Mode
                                    editMode = false
                                },
                                onCancel = {
                                    // Get out of Edit Mode
                                    editMode = false
                                },
                            )
                        } else {
                            DiagnosticImageSection(image = image, onImageEdit = {
                                editMode = true
                            }, onViewResultsClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(
                                        DiagnosisAndAnalysisPages.RESULTS_PAGE.ordinal
                                    )
                                }
                            })
                        }

                    }

                    DiagnosisAndAnalysisPages.RESULTS_PAGE -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Show image number
                            Text(
                                text = "%s %d".format(
                                    stringResource(id = R.string.image_number),
                                    (image.metadata.sample + 1)
                                )
                            )

                            // Get the diagnostic elements
                            val modelDiagnosticElements =
                                image.elements.filterIsInstance<ModelDiagnosticElement>()
                                    .let { if (it.isEmpty()) null else it.toSet() }

                            val specialistDiagnosticElements =
                                image.elements.filterIsInstance<SpecialistDiagnosticElement>()
                                    .let { if (it.isEmpty()) null else it.toSet() }

                            // Show results in a table
                            DiagnosticImageResultsTable(
                                modifier = Modifier.padding(vertical = 8.dp),
                                disease = diagnosis.disease,
                                modelDiagnosticElements = modelDiagnosticElements,
                                specialistDiagnosticElements = specialistDiagnosticElements,
                                modelFailIcon = image.stage == AnalysisStage.NotAnalyzed,
                                onSpecialistEdit = { elementName, specialistDiagnosticElement ->
                                    // Grab the old specialist diagnostic element
                                    val previousDiagnosticElement =
                                        image.elements.firstOrNull { diagnosticElement ->
                                            diagnosticElement is SpecialistDiagnosticElement && diagnosticElement.id == elementName
                                        }   // If value is not null then apply
                                    if (previousDiagnosticElement != null) {
                                        // Invoke the image change callback with new modified image
                                        onImageChange.invoke(
                                            image.copy(elements = image.elements.minus(
                                                previousDiagnosticElement
                                            ).let { oldSet ->
                                                if (specialistDiagnosticElement != null) oldSet.plus(
                                                    specialistDiagnosticElement
                                                ) else oldSet
                                            })
                                        )
                                    } else if (specialistDiagnosticElement != null) {
                                        onImageChange.invoke(
                                            image.copy(
                                                elements = image.elements.union(
                                                    setOf(specialistDiagnosticElement)
                                                )
                                            )
                                        )
                                    }
                                })
                            Spacer(modifier = Modifier.weight(1f))
                            Button(modifier = Modifier.padding(16.dp), onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(
                                        DiagnosisAndAnalysisPages.IMAGE_PAGE.ordinal
                                    )
                                }
                            }) {
                                Text(text = stringResource(id = R.string.goto_image))
                            }
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
            diagnosis = Diagnosis.mock(),
            image = ImageSample.mock(stage = AnalysisStage.NotAnalyzed),
            onAnalyzeAction = {},
            onFinishAction = {},
            onNextAction = {},
            onRepeatAction = {},
            onImageChange = {},
        )
    }
}

@Composable
@Preview
fun DiagnosisAndAnalysisPreview_Analyzed() {
    LeishmaniappTheme {
        DiagnosisAndAnalysisScreen(
            diagnosis = Diagnosis.mock(),
            image = ImageSample.mock(stage = AnalysisStage.Analyzed),
            onAnalyzeAction = {},
            onFinishAction = {},
            onNextAction = {},
            onRepeatAction = {},
            onImageChange = {},
        )
    }
}