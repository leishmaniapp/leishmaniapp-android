package com.leishmaniapp.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.entities.Coordinates
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun DiagnosticImageSection(
    modifier: Modifier = Modifier,
    image: Image,
    onImageEdit: () -> Unit,
    onViewResultsClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (image.processed) {
                TextButton(modifier = Modifier.padding(8.dp), onClick = onImageEdit) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = stringResource(id = R.string.edit_image),
                        modifier = Modifier.padding(end = 8.dp),
                    )
                    Text(text = stringResource(id = R.string.edit_image))
                }
            }
        }

        DiagnosticImage(
            modifier = Modifier.weight(1f),
            image = image,
            selectedElement = null
        ) {}

        Button(
            modifier = Modifier.padding(16.dp), onClick = onViewResultsClick
        ) {
            Text(text = stringResource(id = R.string.goto_results))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DiagnosticImageSectionPreview_NotProcessed() {
    LeishmaniappTheme {
        DiagnosticImageSection(
            image = MockGenerator.mockImage(false),
            onImageEdit = {}
        ) {}
    }
}

@Composable
@Preview(showBackground = true)
fun DiagnosticImageSectionPreview_Processed() {
    LeishmaniappTheme {
        DiagnosticImageSection(
            image = MockGenerator.mockImage(true),
            onImageEdit = {}
        ) {}
    }
}
