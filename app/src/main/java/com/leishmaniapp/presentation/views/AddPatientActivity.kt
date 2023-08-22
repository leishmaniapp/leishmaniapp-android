package com.leishmaniapp.presentation.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.fx.coroutines.continuations.resource
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.AppNameHeader
import kotlin.math.exp

class AddPatientActivity {
}


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
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            AppNameHeader()
            Text(
                text = stringResource(R.string.add_patients),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(20.dp).offset(0.dp, 10.dp)
            )
            Spacer(modifier = Modifier.padding(30.dp))
            Text(
                text = stringResource(R.string.patient_name),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(20.dp)
            )
            TextField(
                value = patientName,
                onValueChange = { patientName = it },
                label = { Text("Ej. Carlos Sarmiento") },
                modifier = Modifier.padding(55.dp, 0.dp, 0.dp, 0.dp).
                clip(RoundedCornerShape(20.dp))
                )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = stringResource(R.string.type_of_id),
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                modifier = Modifier.padding(20.dp)
            )
            //TODO: HACER EL SPINNER

            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = stringResource(R.string.number_id),
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                modifier = Modifier.padding(20.dp)
            )
            TextField(value = numberId, onValueChange = { numberId = it },
                label = { Text("Ej. 1003423789") },
                modifier = Modifier.padding(55.dp, 0.dp, 0.dp, 0.dp).
                clip(RoundedCornerShape(20.dp)))

            Spacer(modifier = Modifier.padding(30.dp))

        }
    }
}


@Preview
@Composable
fun addPatientScreenPreview() {
    addPatientScreen()
}