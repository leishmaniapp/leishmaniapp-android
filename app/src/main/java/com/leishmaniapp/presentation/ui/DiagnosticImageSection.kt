package com.leishmaniapp.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun DiagnosticImageSection(modifier: Modifier = Modifier, image: Image, onButtonClick: () -> Unit) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (image.processed) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = stringResource(id = R.string.edit_image)
                )
            }
        }

        /*TODO: Runtime image loading*/
        

        Button(modifier = Modifier.padding(16.dp), onClick = { /*TODO*/ }) {
            Text(text = stringResource(id = R.string.goto_results))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DiagnosticImageSectionPreview_NotProcessed() {
    LeishmaniappTheme {
        DiagnosticImageSection(
            image = MockGenerator.mockImage(false)
        ) {
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DiagnosticImageSectionPreview_Processed() {
    LeishmaniappTheme {
        DiagnosticImageSection(
            image = MockGenerator.mockImage(true)
        ) {
        }
    }
}
