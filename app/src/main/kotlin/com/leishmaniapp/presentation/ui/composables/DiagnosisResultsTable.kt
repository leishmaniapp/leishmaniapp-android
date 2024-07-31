package com.leishmaniapp.presentation.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.entities.ModelDiagnosticElement
import com.leishmaniapp.domain.entities.SpecialistDiagnosticElement
import com.leishmaniapp.presentation.ui.layout.DataTable
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utilities.mock.MockGenerator.mock

@Composable
fun DiagnosisResultsTable(
    diagnosis: Diagnosis
) {
    DataTable(
        contentAlignment = Alignment.CenterStart,
        cellPaddingValues = PaddingValues(6.dp),
        modifier = Modifier
            .clip(shape = RoundedCornerShape(6.dp))
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(6.dp)
            )
    ) {

        // Disease
        HeadingTableRow { Cell { Text(text = stringResource(id = R.string.disease)) } }
        TableRow { Cell { Text(text = diagnosis.disease.displayName) } }

        // Date and Time
        HeadingTableRow { Cell { Text(text = stringResource(id = R.string.date_time)) } }
        TableRow { Cell { Text(text = diagnosis.date.toString()) } }

        // Patient
        HeadingTableRow { Cell { Text(text = stringResource(id = R.string.patient)) } }
        TableRow { Cell { Text(text = diagnosis.patient.name) } }
        SubheadingTableRow { Cell { Text(text = stringResource(id = R.string.patient_id_document)) } }
        TableRow { Cell { Text(text = diagnosis.patient.document) } }

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
        // Show diagnosis results (positive/negative)
        diagnosis.results.let { r ->
            TableRow {
                Cell {
                    Text(
                        text = stringResource(
                            id =
                            if (r.modelResult) {
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
                            if (r.specialistResult) {
                                R.string.diagnosis_results_positive
                            } else {
                                R.string.diagnosis_results_negative
                            }
                        )
                    )
                }
            }

            // Show each element amount
            diagnosis.disease.elements.forEach { e ->
                HeadingTableRow { Cell { Text(text = e.displayName) } }
                TableRow {
                    Cell {
                        Text(text = r.modelElements[e]?.toString() ?: "0")
                    }
                    Cell {
                        Text(text = r.specialistElements[e]?.toString() ?: "0")
                    }
                }
            }
        }

        // Remarks
        HeadingTableRow { Cell { Text(text = stringResource(id = R.string.remarks)) } }
        TableRow {
            Cell {
                Text(
                    text = diagnosis.remarks ?: stringResource(id = R.string.no_remarks)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DiagnosisResultsTablePreview() {
    LeishmaniappTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            DiagnosisResultsTable(Diagnosis.mock().withResults())
        }
    }
}