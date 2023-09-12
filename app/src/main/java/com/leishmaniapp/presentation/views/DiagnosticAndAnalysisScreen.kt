package com.leishmaniapp.presentation.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.presentation.ui.DiagnosisActionBar
import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import kotlinx.coroutines.launch

internal enum class DiagnosticAndAnalysisPages(
    val title: @Composable () -> Unit,
    val content: @Composable () -> Unit
) {
    ImagePage({ Text(text = stringResource(R.string.tab_image)) }, {}),

    ResultsPage({ Text(text = stringResource(R.string.tab_results)) }, {
    })
}

@Composable
fun DiagnosticAndAnalysisScreen(diagnosis: Diagnosis, image: Image) {

    val pagerState = rememberPagerState(pageCount = { DiagnosticAndAnalysisPages.values().size })

    LeishmaniappScaffold(showHelp = true, bottomBar = {
        DiagnosisActionBar(repeatAction = { /* TODO: On repeat action */ },
            analyzeAction = { /* TODO: On analyze action */ },
            nextAction = { /* TODO: On action next */ },
            finishAction = { /* TODO: On finish action */ })
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
                DiagnosticAndAnalysisPages.values().forEachIndexed { index, item ->
                    Tab(
                        selected = (index == pagerState.currentPage), onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }, text = item.title
                    )
                }
            }

            HorizontalPager(state = pagerState) { page ->
                DiagnosticAndAnalysisPages.values()[page].content.invoke()
            }
        }
    }
}

@Composable
@Preview
fun DiagnosticAndAnalysisPreview() {
    LeishmaniappTheme {
        DiagnosticAndAnalysisScreen(
            diagnosis = MockGenerator.mockDiagnosis(),
            image = MockGenerator.mockImage(processed = false)
        )
    }
}