package com.leishmaniapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.leishmaniapp.entities.Patient
import com.leishmaniapp.infrastructure.persistance.ApplicationDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class PatientsViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val applicationDatabase: ApplicationDatabase,
) : ViewModel() {

    val patients: Flow<List<Patient>> = applicationDatabase.patientsRepository().allPatientsFlow()
    var currentPatient: Patient? = savedStateHandle["currentPatient"]
        set(value) {
            savedStateHandle["currentPatient"] = value
            field = value
        }

    fun addNewPatient(patient: Patient) {
        runBlocking {
            applicationDatabase.patientsRepository().upsertPatient(patient)
        }
    }
}