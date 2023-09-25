package com.leishmaniapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.leishmaniapp.entities.Patient
import com.leishmaniapp.entities.mock.MockGenerator

class PatientsViewModel: ViewModel() {
    /*lISTA DE PACIENTES MOCK*/
    val patient1 = MockGenerator.mockPatient()
    val patient2 = MockGenerator.mockPatient()
    val patient3 = MockGenerator.mockPatient()
    val patient4 = MockGenerator.mockPatient()

    /*TODO: pull to room dartabase*/
    val patientSet : Set<Patient> = setOf(patient1, patient2, patient3, patient4)




}

