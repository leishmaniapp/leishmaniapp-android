package com.leishmaniapp.presentation.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leishmaniapp.R
import com.leishmaniapp.entities.DiagnosticElementName
import com.leishmaniapp.entities.ModelDiagnosticElement
import com.leishmaniapp.entities.SpecialistDiagnosticElement
import com.leishmaniapp.entities.disease.Disease
import com.leishmaniapp.entities.disease.MockDotsDisease
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utils.MockGenerator

@Composable
fun DiagnosticImageResultsTable(
    modifier: Modifier = Modifier,
    disease: Disease,
    modelDiagnosticElements: Set<ModelDiagnosticElement>?,
    specialistDiagnosticElements: Set<SpecialistDiagnosticElement>?,
    modelFailIcon: Boolean = false,
    onSpecialistEdit: (DiagnosticElementName, SpecialistDiagnosticElement?) -> Unit
) {
    DataTable(
        modifier = modifier,
        cellPaddingValues = PaddingValues(6.dp)
    ) {
        /* Header */
        HeadingTableRow {
            Cell {
                Text(
                    text = stringResource(id = R.string.characteristic),
                    style = TextStyle(fontSize = 14.sp)
                )
            }
            Cell {
                Text(
                    text = stringResource(id = R.string.model),
                    style = TextStyle(fontSize = 14.sp)
                )
            }
            Cell {
                Text(
                    text = stringResource(id = R.string.specialist),
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        }

        disease.elements.forEach { diagnosticElementName ->
            TableRow(contentAlignment = Alignment.Center) {
                Cell {
                    Text(
                        text = diagnosticElementName.displayName,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                // Model
                Cell {
                    if (modelDiagnosticElements == null) {
                        if (modelFailIcon) {
                            Icon(
                                Icons.Filled.Error,
                                contentDescription = stringResource(id = R.string.fail),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                    } else {
                        Text(text = modelDiagnosticElements.first { it.id == diagnosticElementName }.amount.toString())
                    }
                }
                // Specialist
                Cell {
                    // Grab the diagnostic element
                    val specialistDiagnosticElement =
                        specialistDiagnosticElements?.firstOrNull { it.id == diagnosticElementName }

                    BasicTextField(
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = LocalTextStyle.current,
                        onValueChange = { newValue ->
                            try {
                                onSpecialistEdit.invoke(
                                    diagnosticElementName,
                                    specialistDiagnosticElement?.copy(
                                        amount = newValue.replace(
                                            "-",
                                            ""
                                        ).toInt()
                                    ) ?: SpecialistDiagnosticElement(
                                        diagnosticElementName,
                                        newValue.replace("-", "").toInt()
                                    )
                                )
                            } catch (_: NumberFormatException) {
                                onSpecialistEdit.invoke(diagnosticElementName, null)
                            }
                        },
                        value = specialistDiagnosticElement?.amount?.toString() ?: "-"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultsTablePreview_NoneProvided() {
    var specialistDiagnosticElement: SpecialistDiagnosticElement? by remember {
        mutableStateOf(null)
    }

    LeishmaniappTheme {
        DiagnosticImageResultsTable(
            disease = MockDotsDisease,
            specialistDiagnosticElements = specialistDiagnosticElement?.let { setOf(it) },
            modelDiagnosticElements = null,
        ) { _, editedSpecialistDiagnosticElement ->
            specialistDiagnosticElement = editedSpecialistDiagnosticElement
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultsTablePreview_FailedModel() {
    var specialistDiagnosticElement: SpecialistDiagnosticElement? by remember {
        mutableStateOf(null)
    }

    LeishmaniappTheme {
        DiagnosticImageResultsTable(
            disease = MockDotsDisease,
            specialistDiagnosticElements = specialistDiagnosticElement?.let { setOf(it) },
            modelDiagnosticElements = null,
            modelFailIcon = true
        ) { _, editedSpecialistDiagnosticElement ->
            specialistDiagnosticElement = editedSpecialistDiagnosticElement
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultsTablePreview_OnlyModelProvided() {
    var specialistDiagnosticElement: SpecialistDiagnosticElement? by remember {
        mutableStateOf(null)
    }

    LeishmaniappTheme {
        DiagnosticImageResultsTable(
            disease = MockDotsDisease,
            specialistDiagnosticElements = specialistDiagnosticElement?.let { setOf(it) },
            modelDiagnosticElements = setOf(MockGenerator.mockModelDiagnosticElement())
        ) { _, editedSpecialistDiagnosticElement ->
            specialistDiagnosticElement = editedSpecialistDiagnosticElement
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultsTablePreview_OnlySpecialistProvided() {
    var specialistDiagnosticElement: SpecialistDiagnosticElement? by remember {
        mutableStateOf(MockGenerator.mockSpecialistDiagnosticElement())
    }

    LeishmaniappTheme {
        DiagnosticImageResultsTable(
            disease = MockDotsDisease,
            specialistDiagnosticElements = specialistDiagnosticElement?.let { setOf(it) },
            modelDiagnosticElements = null,
        ) { _, editedSpecialistDiagnosticElement ->
            specialistDiagnosticElement = editedSpecialistDiagnosticElement
        }
    }
}

@Preview(showBackground = true, widthDp = 380)
@Composable
fun ResultsTablePreview_TableIsShort() {
    LeishmaniappTheme {
        DiagnosticImageResultsTable(
            disease = MockDotsDisease,
            specialistDiagnosticElements = null,
            modelDiagnosticElements = null,
        ) { _, _ -> }
    }
}
