package com.leishmaniapp.presentation.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.entities.ModelDiagnosticElement
import com.leishmaniapp.entities.SpecialistDiagnosticElement
import com.leishmaniapp.entities.mock.MockGenerator
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun ResultsTable(modelDiagnosticElement: ModelDiagnosticElement?, specialistDiagnosticElement: SpecialistDiagnosticElement?){
    var progress by remember { mutableStateOf(0.1f) }
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    ).value
    LeishmaniappScaffold {
        Column {
            // Spacer(modifier = Modifier.padding(10.dp))
            Row {
                Text(text = stringResource(R.string.number_of_image), modifier = Modifier.padding(10.dp, 10.dp, 5.dp,5.dp),
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.scrim)
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Column(modifier = Modifier.padding(1.dp)){
                DataTable(
                    contentAlignment = Alignment.CenterStart,
                    cellPaddingValues = PaddingValues(6.dp),
                    modifier = Modifier.border(
                        BorderStroke(1.dp, MaterialTheme.colorScheme.background)
                    )
                ) {
                    SubheadingTableRow {
                        Cell { Text(text = stringResource(id = R.string.characteristic),
                            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.scrim
                        ) }
                        Cell { Text(text = stringResource(id = R.string.model),
                            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.scrim) }
                        Cell { Text(text = stringResource(id = R.string.specialist),
                            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.scrim) }
                    }
                    SubheadingTableRow {
                        Cell { Text(text = stringResource(id = R.string.macrofages))}
                        Cell {
                            if(modelDiagnosticElement?.amount != null){
                                Text(text = modelDiagnosticElement.amount.toString())
                            }else{
                                CircularProgressIndicator(progress = animatedProgress)
                            }
                        }
                        Cell {
                            if(specialistDiagnosticElement?.amount != null){
                                Text(text = specialistDiagnosticElement.amount.toString())
                            }else if(specialistDiagnosticElement?.amount == null && modelDiagnosticElement?.amount != null){
                                Text(text = modelDiagnosticElement.amount.toString())
                            }else{
                                Text(text = "-")
                            }
                        }
                    }
                    SubheadingTableRow {
                        Cell { Text(text = stringResource(id = R.string.parasites))}
                        Cell {
                            if(modelDiagnosticElement?.amount != null){
                                Text(text = modelDiagnosticElement.amount.toString())
                            }else{
                                CircularProgressIndicator()
                            }
                        }
                        Cell {
                            if(specialistDiagnosticElement?.amount != null){
                                Text(text = specialistDiagnosticElement.amount.toString())
                            }else if(specialistDiagnosticElement?.amount == null && modelDiagnosticElement?.amount != null){
                                Text(text = modelDiagnosticElement.amount.toString())
                            }else{
                                Text(text = "-")
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(200.dp))
            reusableButton(stringResource(id = R.string.see_imge))
        }
    }
}


@Preview
@Composable
fun ResultsTablepreview(){
    LeishmaniappTheme {
        ResultsTable(modelDiagnosticElement = MockGenerator.mockModelDiagnosticElement(), specialistDiagnosticElement = MockGenerator.mockSpecialistDiagnosticElement())
    }
}

@Preview
@Composable
fun ResultsTablepreview_null(){
    LeishmaniappTheme {
        ResultsTable(modelDiagnosticElement = null, specialistDiagnosticElement = null)
    }
}
