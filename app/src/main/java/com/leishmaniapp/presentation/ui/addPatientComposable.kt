package com.leishmaniapp.presentation.ui


import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leishmaniapp.R

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ResourceType")
@Composable
fun addPatientScreen() {
    var patientName by remember { mutableStateOf("") }
    var numberId by remember { mutableStateOf("") }
    var expanded by remember {
        mutableStateOf(false)
    }
    val typesOfId: List<String> = stringArrayResource(R.array.typeOfId).toList()
    var selectedTypeOfId by remember { mutableStateOf(" ") }
    var selectedOptionText by remember { mutableStateOf(typesOfId[0]) }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            AppNameHeader()
            Column(modifier = Modifier.padding(20.dp) ){
                Text(
                    text = stringResource(R.string.add_patients),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .offset(0.dp, 10.dp)
                )
                Spacer(modifier = Modifier.padding(30.dp))
                Text(
                    text = stringResource(R.string.patient_name),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                TextField(
                    value = patientName,
                    onValueChange = { patientName = it },
                    label = { Text("Ej. Carlos Sarmiento") },
                    modifier = Modifier
                        .width(400.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                )
            }

            Spacer(modifier = Modifier.padding(5.dp))

            Column(modifier = Modifier.padding(20.dp) ){
                Text(
                    text = stringResource(R.string.type_of_id),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
                //TODO: HACER EL SPINNER

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange ={expanded = !expanded},
                    modifier = Modifier
                        .align(Alignment.Start)
                        .clip(RoundedCornerShape(10.dp)))
                {
                    TextField(value = selectedOptionText,
                        onValueChange = {},
                        readOnly = true,
                        label = {},
                        modifier = Modifier.width(400.dp),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()

                    )
                    ExposedDropdownMenu(expanded = expanded,
                        onDismissRequest = { false
                        },
                    ) {
                        typesOfId.forEach { selectionOption ->
                            DropdownMenuItem(text = { Text(selectionOption) },
                                onClick = {selectedOptionText = selectionOption
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.padding(5.dp))

            Column(modifier = Modifier.padding(20.dp) ){
                Text(
                    text = stringResource(R.string.number_id),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
                TextField(value = numberId, onValueChange = { numberId = it },
                    label = { Text("Ej. 1003423789") },
                    modifier = Modifier
                        .width(400.dp)
                        .clip(RoundedCornerShape(10.dp)))
            }


            Spacer(modifier = Modifier.padding(50.dp))
        }
    }
}


@Preview
@Composable
fun addPatientScreenPreview() {
    addPatientScreen()
}