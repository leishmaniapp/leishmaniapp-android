package com.leishmaniapp.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.DiagnosticElement
import com.leishmaniapp.entities.ModelDiagnosticElement
import com.leishmaniapp.entities.SpecialistDiagnosticElement
import com.leishmaniapp.entities.leishmaniasis.giemsa.LeishmaniasisGiemsaDisease
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import kotlin.reflect.KClass

@Composable
fun DiagnosisResultsTable(
    diagnosis: Diagnosis
) {
    DataTable(
        contentAlignment = Alignment.CenterStart,
        cellPaddingValues = PaddingValues(6.dp)
    ) {
        // Disease
        HeadingTableRow { Cell { Text(text = stringResource(id = R.string.disease)) } }
        // TODO: Provide some sort of translation provider
        TableRow { Cell { Text(text = diagnosis.diagnosticDisease.id) } }
        // Date and Time
        HeadingTableRow { Cell { Text(text = stringResource(id = R.string.date_time)) } }
        TableRow { Cell { Text(text = diagnosis.date.toString()) } }
        // Patient
        HeadingTableRow { Cell { Text(text = stringResource(id = R.string.patient)) } }
        TableRow { Cell { Text(text = diagnosis.patientDiagnosed.name) } }
        SubheadingTableRow { Cell { Text(text = stringResource(id = R.string.patient_id_document)) } }
        TableRow { Cell { Text(text = diagnosis.patientDiagnosed.documentString) } }
        // Specialist
        HeadingTableRow { Cell { Text(text = stringResource(id = R.string.specialist)) } }
        TableRow { Cell { Text(text = diagnosis.specialist.name) } }
        // Number of samples
        HeadingTableRow { Cell { Text(text = stringResource(id = R.string.number_of_samples)) } }
        TableRow { Cell { Text(text = diagnosis.samples.toString()) } }

        //! Diagnosis Results
        SubheadingTableRow {
            Cell { Text(text = stringResource(id = R.string.model)) }
            Cell { Text(text = stringResource(id = R.string.specialist)) }
        }

        HeadingTableRow { Cell { Text(text = stringResource(id = R.string.diagnosis_results)) } }
        TableRow {
            Cell {
                Text(
                    text = stringResource(
                        id =
                        if (diagnosis.modelResult) {
                            R.string.diagnosis_results_positive
                        } else {
                            R.string.diagnosis_results_negative
                        }
                    )
                )
            }
            Cell {
                Text(
                    text = stringResource(
                        id =
                        if (diagnosis.specialistResult) {
                            R.string.diagnosis_results_positive
                        } else {
                            R.string.diagnosis_results_negative
                        }
                    )
                )
            }
        }

        diagnosis.computedResults.forEach { (element, data) ->
            //TODO: Provide some sort of translation provider
            HeadingTableRow { Cell { Text(text = element) } }
            TableRow {
                Cell {
                    Text(text = data[ModelDiagnosticElement::class].toString())
                }
                Cell {
                    Text(text = data[SpecialistDiagnosticElement::class].toString())
                }
            }
        }

        // Remarks
        HeadingTableRow { Cell { Text(text = stringResource(id = R.string.remarks)) } }
        TableRow { Cell { Text(text = diagnosis.remarks) } }
    }
}

@Composable
@Preview(showBackground = true)
fun DiagnosisResultsTablePreview() {
    LeishmaniappTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            DiagnosisResultsTable(MockGenerator.mockDiagnosis().apply {
                computeResults()
            })
        }
    }
}