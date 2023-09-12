package com.leishmaniapp.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.entities.Coordinates
import com.leishmaniapp.entities.DiagnosticElement
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ModelDiagnosticElement
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * TODO: Exhaustive testing is required for this functionality
 */
@Composable
fun DiagnosticImageEditSection(
    modifier: Modifier = Modifier,
    image: Image,
    onCompleted: (Boolean, List<ModelDiagnosticElement>) -> Unit,
) {
    val imageModelDiagnosticElements: MutableList<ModelDiagnosticElement> =
        image.diagnosticElements.filterIsInstance<ModelDiagnosticElement>().toMutableList()

    var mutableImage by remember {
        mutableStateOf(image)
    }

    var selectedElement: Pair<String, Coordinates>? by remember {
        mutableStateOf(null)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = { onCompleted.invoke(true, imageModelDiagnosticElements) }) {
                Icon(
                    Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.accept),
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = stringResource(id = R.string.accept))
            }

            TextButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = { onCompleted.invoke(false, imageModelDiagnosticElements) }) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = stringResource(id = R.string.discart),
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = stringResource(id = R.string.discart))
            }
        }

        DiagnosticImage(
            modifier = Modifier.weight(1f),
            image = mutableImage,
            selectedElement = selectedElement,
            onElementPressed = {
                selectedElement = it
            })


        Column(
            modifier = Modifier
                .padding(18.dp)
                .alpha(if (selectedElement == null) 0f else 1f)
                .fillMaxWidth()
        ) {
            Text(
                text = String.format(
                    "%s %s?",
                    stringResource(id = R.string.erase_element),
                    selectedElement?.first
                ),
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                FilledTonalButton(onClick = {
                    selectedElement = null;
                }) {
                    Text(text = stringResource(id = R.string.cancel))
                }

                Button(onClick = {
                    // Find the element
                    val diagnosticElement = imageModelDiagnosticElements.first {
                        it.name == selectedElement?.first
                    }
                    // Remove the coordinates
                    diagnosticElement.coordinates.remove(selectedElement!!.second)
                    // Update the image
                    mutableImage = image.copy(
                        diagnosticElements = imageModelDiagnosticElements.toMutableList()
                    )
                    selectedElement = null
                }) {
                    Text(text = stringResource(id = R.string.erase))
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DiagnosticImageEditSectionPreview() {
    LeishmaniappTheme {
        DiagnosticImageEditSection(
            image = MockGenerator.mockImage(true),
        ) { accept, elements -> }
    }
}