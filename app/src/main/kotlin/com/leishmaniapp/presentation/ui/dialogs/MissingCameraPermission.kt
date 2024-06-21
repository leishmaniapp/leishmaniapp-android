package com.leishmaniapp.presentation.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun MissingCameraPermission(
    modifier: Modifier = Modifier,
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Filled.Warning,
            contentDescription = stringResource(id = R.string.missing_camera_permission),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(16.dp)
                .size(32.dp)
        )
        Text(
            text = stringResource(id = R.string.missing_camera_permission),
            textAlign = TextAlign.Center
        )
        Button(modifier = Modifier.padding(16.dp), onClick = onRequestPermission) {
            Text(text = stringResource(id = R.string.request_permission))
        }
    }
}

@Composable
@Preview
fun MissingCameraPermissionPreview() {
    LeishmaniappTheme {
        MissingCameraPermission {}
    }
}