//package com.leishmaniapp.presentation.ui.views.patients
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.ExposedDropdownMenuBox
//import androidx.compose.material3.ExposedDropdownMenuDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.leishmaniapp.R
//import com.leishmaniapp.entities.DocumentType
//import com.leishmaniapp.entities.IdentificationDocument
//import com.leishmaniapp.entities.Patient
//import com.leishmaniapp.presentation.ui.layout.LeishmaniappScaffold
//import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
//
//const val inlinePadding = 2
//const val componentSeparatorHeight = 18
//
//@Composable
//fun AddPatientScreen(backButtonAction: (() -> Unit)? = null, onCreatePatient: (Patient) -> Unit) {
//
//    var patientNameField: String by remember { mutableStateOf("") }
//    var documentIdField: String by remember { mutableStateOf("") }
//    var documentTypeField: DocumentType? by remember { mutableStateOf(null) }
//    var documentTypeExpanded: Boolean by remember { mutableStateOf(false) }
//
//    var invalidFields by remember {
//        mutableStateOf(false)
//    }
//
//    LeishmaniappScaffold(
//        title = stringResource(R.string.patients), backButtonAction = backButtonAction
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(horizontal = 16.dp)
//                .padding(bottom = 0.dp, top = 20.dp)
//                .fillMaxSize()
//        ) {
//
//            Text(
//                text = stringResource(R.string.patient_add),
//                style = MaterialTheme.typography.headlineMedium,
//                modifier = Modifier.padding(bottom = componentSeparatorHeight.dp)
//            )
//
//            /* -- Patient name -- */
//            Spacer(modifier = Modifier.height(componentSeparatorHeight.dp))
//            Text(
//                text = stringResource(id = R.string.patient_name),
//                style = MaterialTheme.typography.labelLarge,
//                modifier = Modifier.padding(inlinePadding.dp)
//            )
//            OutlinedTextField(modifier = Modifier
//                .fillMaxWidth()
//                .padding(inlinePadding.dp),
//                value = patientNameField,
//                onValueChange = { patientNameField = it },
//                placeholder = { Text(text = stringResource(id = R.string.patient_name_field_placeholder)) })
//
//            /* -- Patient document type -- */
//            Spacer(modifier = Modifier.height(componentSeparatorHeight.dp))
//            Text(
//                text = stringResource(id = R.string.document_type),
//                style = MaterialTheme.typography.labelLarge,
//                modifier = Modifier.padding(inlinePadding.dp)
//            )
//            ExposedDropdownMenuBox(modifier = Modifier
//                .fillMaxWidth()
//                .padding(inlinePadding.dp),
//                expanded = documentTypeExpanded,
//                onExpandedChange = { documentTypeExpanded = !documentTypeExpanded }) {
//
//                OutlinedTextField(
//                    modifier = Modifier
//                        .menuAnchor()
//                        .fillMaxWidth(),
//                    readOnly = true,
//                    value = documentTypeField?.name ?: "",
//                    onValueChange = { },
//                    placeholder = { Text(text = stringResource(id = R.string.document_type_field_placeholder)) },
//                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = documentTypeExpanded) },
//                )
//
//                ExposedDropdownMenu(expanded = documentTypeExpanded,
//                    onDismissRequest = { documentTypeExpanded = false }) {
//                    DocumentType.values().forEach { documentType ->
//                        DropdownMenuItem(text = { Text(text = documentType.name) }, onClick = {
//                            documentTypeField = DocumentType.valueOf(documentType.name)
//                            documentTypeExpanded = false
//                        }, contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
//                        )
//                    }
//                }
//            }
//
//            /* -- Patient Document ID -- */
//            Spacer(modifier = Modifier.height(componentSeparatorHeight.dp))
//            Text(
//                text = stringResource(id = R.string.document_id),
//                style = MaterialTheme.typography.labelLarge,
//                modifier = Modifier.padding(inlinePadding.dp)
//            )
//            OutlinedTextField(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(inlinePadding.dp),
//                value = documentIdField,
//                onValueChange = { documentIdField = it },
//                placeholder = { Text(text = stringResource(id = R.string.document_id_field_placeholder)) },
//            )
//
//            Spacer(modifier = Modifier.weight(1f))
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(32.dp)
//            ) {
//                Button(modifier = Modifier
//                    .padding(top = 16.dp)
//                    .align(Alignment.Center), onClick = {
//
//                    if (patientNameField.isNotBlank()
//                        && documentIdField.isNotBlank()
//                        && documentTypeField != null
//                    ) {
//                        onCreatePatient.invoke(
//                            Patient(
//                                patientNameField,
//                                IdentificationDocument(documentIdField),
//                                documentTypeField!!
//                            )
//                        )
//                    } else {
//                        invalidFields = true
//                    }
//                }) {
//                    Text(text = stringResource(id = R.string.patient_create))
//                }
//            }
//        }
//    }
//
//    if (invalidFields) {
//        AlertDialog(onDismissRequest = { invalidFields = false }) {
//            Card {
//                Text(
//                    modifier = Modifier.padding(16.dp),
//                    text = stringResource(id = R.string.alert_invalid_fields)
//                )
//            }
//        }
//    }
//}
//
//
//@Preview
//@Composable
//fun AddPatientScreenPreview() {
//    LeishmaniappTheme {
//        AddPatientScreen(backButtonAction = {}) {}
//    }
//}