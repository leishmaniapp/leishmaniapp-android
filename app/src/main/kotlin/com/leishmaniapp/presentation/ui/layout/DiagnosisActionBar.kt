package com.leishmaniapp.presentation.ui.layout

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun DiagnosisActionBar(
    analysisStage: AnalysisStage = AnalysisStage.NotAnalyzed,
    analysisShowAsLoading: Boolean = false,
    repeatAction: () -> Unit,
    analyzeAction: () -> Unit,
    nextAction: () -> Unit,
    finishAction: () -> Unit,
) {
    NavigationBar {
        NavigationBarItem(selected = false, onClick = repeatAction, icon = {
            Icon(
                Icons.Filled.Refresh,
                contentDescription = stringResource(id = R.string.action_repeat)
            )
        }, label = {
            Text(text = stringResource(id = R.string.action_repeat))
        })

        NavigationBarItem(enabled = analysisStage == AnalysisStage.NotAnalyzed,
            selected = false,
            onClick = if (analysisShowAsLoading && analysisStage == AnalysisStage.NotAnalyzed) {
                {}
            } else {
                analyzeAction
            },
            icon = {
                if (analysisShowAsLoading && analysisStage == AnalysisStage.NotAnalyzed) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = stringResource(id = R.string.action_analyze)
                    )
                }
            },
            label = {
                Text(text = stringResource(id = R.string.action_analyze))
            })

        NavigationBarItem(selected = false, onClick = nextAction, icon = {
            Icon(
                if (analysisStage == AnalysisStage.Deferred || analysisStage == AnalysisStage.Analyzed) {
                    Icons.Filled.CameraAlt
                } else {
                    Icons.AutoMirrored.Filled.ArrowForward
                }, contentDescription = stringResource(id = R.string.action_next)
            )
        }, label = {
            Text(text = stringResource(id = R.string.action_next))
        })

        NavigationBarItem(selected = false, onClick = finishAction, icon = {
            Icon(
                Icons.Filled.Done,
                contentDescription = stringResource(id = R.string.action_finish)
            )
        }, label = {
            Text(text = stringResource(id = R.string.action_finish))
        })
    }
}

@Composable
@Preview
fun DiagnosisActionBarPreview_NextArrow() {
    LeishmaniappTheme {
        DiagnosisActionBar(
            repeatAction = {},
            analyzeAction = {},
            nextAction = {},
            finishAction = {},
        )
    }
}

@Composable
@Preview
fun DiagnosisActionBarPreview_NextCamera() {
    LeishmaniappTheme {
        DiagnosisActionBar(repeatAction = {},
            analyzeAction = {},
            nextAction = {},
            finishAction = {},
            analysisStage = AnalysisStage.Analyzed
        )
    }
}

@Composable
@Preview
fun DiagnosisActionBarPreview_ShowAsLoading() {
    LeishmaniappTheme {
        DiagnosisActionBar(
            repeatAction = {},
            analyzeAction = {},
            nextAction = {},
            finishAction = {},
            analysisShowAsLoading = true,
        )
    }
}