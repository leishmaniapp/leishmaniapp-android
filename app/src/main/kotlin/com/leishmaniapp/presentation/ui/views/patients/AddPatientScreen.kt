package com.leishmaniapp.presentation.ui.views.patients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.leishmaniapp.domain.entities.DocumentType
import com.leishmaniapp.domain.entities.Patient
import com.leishmaniapp.domain.types.Identificator
import com.leishmaniapp.presentation.ui.layout.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPatientScreen(
    backButtonAction: (() -> Unit)? = null,
    onCreatePatient: (String, Identificator, DocumentType) -> Unit,
) {

    var patientNameField: String by remember { mutableStateOf("") }
    var documentIdField: Identificator by remember { mutableStateOf("") }
    var documentTypeField: DocumentType by remember { mutableStateOf(DocumentType.CC) }
    var documentTypeExpanded: Boolean by remember { mutableStateOf(false) }

    LeishmaniappScaffold(
        title = stringResource(R.string.patients),
        backButtonAction = backButtonAction
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            Text(
                modifier = Modifier.padding(vertical = 20.dp),
                text = stringResource(R.string.patient_add),
                style = MaterialTheme.typography.headlineMedium,
            )

            /* -- Patient name -- */
            Text(
                text = stringResource(id = R.string.patient_name),
                style = MaterialTheme.typography.labelLarge,
            )
            OutlinedTextField(modifier = Modifier
                .fillMaxWidth(),
                value = patientNameField,
                onValueChange = { patientNameField = it },
                placeholder = { Text(text = stringResource(id = R.string.patient_name_field_placeholder)) })

            /* -- Patient document type -- */
            Text(
                text = stringResource(id = R.string.document_type),
                style = MaterialTheme.typography.labelLarge,
            )
            ExposedDropdownMenuBox(modifier = Modifier
                .fillMaxWidth(),
                expanded = documentTypeExpanded,
                onExpandedChange = { documentTypeExpanded = !documentTypeExpanded }) {

                OutlinedTextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    value = documentTypeField.name,
                    onValueChange = { },
                    placeholder = { Text(text = stringResource(id = R.string.document_type_field_placeholder)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = documentTypeExpanded) },
                )

                ExposedDropdownMenu(expanded = documentTypeExpanded,
                    onDismissRequest = { documentTypeExpanded = false }) {
                    DocumentType.entries.forEach { documentType ->
                        DropdownMenuItem(text = { Text(text = documentType.name) }, onClick = {
                            documentTypeField = DocumentType.valueOf(documentType.name)
                            documentTypeExpanded = false
                        }, contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            /* -- Patient Document ID -- */
            Text(
                text = stringResource(id = R.string.document_id),
                style = MaterialTheme.typography.labelLarge,
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = documentIdField,
                onValueChange = { documentIdField = it },
                placeholder = { Text(text = stringResource(id = R.string.document_id_field_placeholder)) },
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                Button(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = {
                        onCreatePatient.invoke(
                            patientNameField,
                            documentIdField,
                            documentTypeField,
                        )
                    }
                ) {
                    Text(text = stringResource(id = R.string.patient_create))
                }
            }
        }
    }
}


@Preview
@Composable
fun AddPatientScreenPreview() {
    LeishmaniappTheme {
        AddPatientScreen(backButtonAction = {}) { _, _, _ -> }
    }
}