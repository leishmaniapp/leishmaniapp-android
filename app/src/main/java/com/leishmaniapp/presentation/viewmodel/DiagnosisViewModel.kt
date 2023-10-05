package com.leishmaniapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.Patient
import com.leishmaniapp.entities.mock.MockGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiagnosisViewModel @Inject constructor(): ViewModel() {

    val diagnosis = MockGenerator.mockDiagnosis() /*TODO: ROOM*/

    val diagnosis1 = MockGenerator.mockDiagnosis() /*TODO: ROOM*/
    val diagnosis2 = MockGenerator.mockDiagnosis() /*TODO: ROOM*/
    val diagnosis3 = MockGenerator.mockDiagnosis() /*TODO: ROOM*/
    val diagnosis4 = MockGenerator.mockDiagnosis() /*TODO: ROOM*/

    val diagnosisList : List<Diagnosis> = listOf(diagnosis1, diagnosis2, diagnosis3, diagnosis4)

    val actualImage = MockGenerator.mockImage()

}