package com.leishmaniapp.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun DiagnosisActionBar(
    repeatAction: () -> Unit,
    analyzeAction: () -> Unit,
    nextAction: () -> Unit,
    finishAction: () -> Unit,
    nextIsCamera: Boolean = false,
) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = repeatAction,
            icon = {
                Icon(
                    Icons.Filled.Refresh,
                    contentDescription = stringResource(id = R.string.action_repeat)
                )
            }, label = {
                Text(text = stringResource(id = R.string.action_repeat))
            })

        NavigationBarItem(
            selected = false,
            onClick = analyzeAction,
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = stringResource(id = R.string.action_analyze)
                )
            }, label = {
                Text(text = stringResource(id = R.string.action_analyze))
            })

        NavigationBarItem(
            selected = false,
            onClick = nextAction,
            icon = {
                Icon(
                    if (nextIsCamera) {
                        Icons.Filled.CameraAlt
                    } else {
                        Icons.AutoMirrored.Filled.ArrowForward
                    },
                    contentDescription = stringResource(id = R.string.action_next)
                )
            }, label = {
                Text(text = stringResource(id = R.string.action_next))
            })

        NavigationBarItem(
            selected = false,
            onClick = finishAction,
            icon = {
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
            finishAction = {}, nextIsCamera = false
        )
    }
}

@Composable
@Preview
fun DiagnosisActionBarPreview_NextCamera() {
    LeishmaniappTheme {
        DiagnosisActionBar(
            repeatAction = {},
            analyzeAction = {},
            nextAction = {},
            finishAction = {}, nextIsCamera = true
        )
    }
}