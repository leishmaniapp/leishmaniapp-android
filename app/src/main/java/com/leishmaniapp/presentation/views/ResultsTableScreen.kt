package com.leishmaniapp.presentation.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.ModelDiagnosticElement
import com.leishmaniapp.entities.Patient
import com.leishmaniapp.entities.SpecialistDiagnosticElement
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.ResultsTable
import com.leishmaniapp.presentation.ui.ReusableTopBar
import com.leishmaniapp.presentation.ui.ReusableButton
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
@Composable
fun ResultsTableScreen(patient: Patient, image: Image, modelDiagnosticElement: ModelDiagnosticElement?, specialistDiagnosticElement: SpecialistDiagnosticElement?){
   LeishmaniappScaffold {
       Column {
           ReusableTopBar(text = stringResource(id = R.string.patient) + " " + patient.name)
           Row(modifier = Modifier
               .fillMaxWidth()
               .padding(5.dp, 10.dp, 5.dp, 5.dp)
           ){
               Text(text =  stringResource(id = R.string.number_of_image) + " " + image.sample.toString())
           }
           ResultsTable(modelDiagnosticElement = modelDiagnosticElement, specialistDiagnosticElement = specialistDiagnosticElement )
           Spacer(modifier = Modifier.padding(120.dp))
           ReusableButton(titleButton = stringResource(id = R.string.see_imge))
       }
   }
}

@Preview
@Composable
fun ResultsTablepreview(){
    LeishmaniappTheme {
        ResultsTableScreen(patient = MockGenerator.mockPatient(), image = MockGenerator.mockImage(), MockGenerator.mockModelDiagnosticElement(), specialistDiagnosticElement = MockGenerator.mockSpecialistDiagnosticElement())
    }
}
