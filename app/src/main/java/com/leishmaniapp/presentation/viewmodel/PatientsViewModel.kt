package com.leishmaniapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.leishmaniapp.entities.Patient
import com.leishmaniapp.persistance.database.ApplicationDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class PatientsViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val applicationDatabase: ApplicationDatabase,
) : ViewModel() {

    val patients: Set<Patient> by lazy {
        runBlocking {
            applicationDatabase.patientDao().allPatients().toSet()
        }
    }

    var currentPatient: Patient? = savedStateHandle["currentPatient"]
        set(value) {
            savedStateHandle["currentPatient"] = value
            field = value
        }
}

