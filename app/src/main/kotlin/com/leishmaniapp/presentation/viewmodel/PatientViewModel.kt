package com.leishmaniapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.entities.DocumentType
import com.leishmaniapp.domain.entities.Patient
import com.leishmaniapp.domain.exceptions.BadInputException
import com.leishmaniapp.domain.exceptions.GenericException
import com.leishmaniapp.domain.repository.IDiagnosesRepository
import com.leishmaniapp.domain.repository.IPatientsRepository
import com.leishmaniapp.domain.types.Identificator
import com.leishmaniapp.presentation.viewmodel.state.PatientState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Create and manipulate [Patient] data
 */
@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class PatientViewModel @Inject constructor(

    /**
     * Keep state accross configuration changes
     */
    savedStateHandle: SavedStateHandle,

    // Repositories
    private val patientsRepository: IPatientsRepository,
    private val diagnosesRepository: IDiagnosesRepository,

    ) : ViewModel() {

    /**
     * List of all patients in database
     */
    val patients: StateFlow<List<Patient>> =
        patientsRepository.allPatients().stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            listOf()
        )

    private val _state: MutableLiveData<PatientState> =
        savedStateHandle.getLiveData("state", PatientState.None)

    /**
     * Current operation state
     */
    val state: LiveData<PatientState> = _state

    private val _selectedPatient: MutableLiveData<Patient?> =
        savedStateHandle.getLiveData("selectedPatient", null)

    /**
     * Current selected patient
     */
    val selectedPatient: LiveData<Patient?> = _selectedPatient

    /**
     * Get the list of diagnoses for a selected patient
     */
    val diagnosesForSelectedPatient: StateFlow<List<Diagnosis>> =
        selectedPatient.asFlow().flatMapMerge { patient ->
            if (patient != null) {
                diagnosesRepository.diagnosesForPatient(patient)
            } else {
                flowOf(listOf())
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    /**
     * Store a new patient in database
     */
    fun createNewPatient(name: String, id: Identificator, type: DocumentType) {
        _state.value = PatientState.Busy
        viewModelScope.launch {

            // Validate input
            if (name.isEmpty() || id.isEmpty()) {
                _state.value = PatientState.Error(
                    BadInputException()
                )
            }

            // Insert the new patient in database
            try {
                patientsRepository.upsertPatient(
                    Patient(
                        name = name,
                        id = id,
                        documentType = type
                    )
                )
            } catch (e: Exception) {
                _state.value = PatientState.Error(
                    GenericException(e)
                )
            }

            // Reset back the state
            _state.value = PatientState.Success
        }
    }

    /**
     * Select a patient for retrieval
     */
    fun selectPatient(patient: Patient) {
        _selectedPatient.value = patient
    }

    /**
     * Undo patient selection
     */
    fun dismissPatient() {
        _selectedPatient.value = null
    }

    /**
     * Reset the current [PatientState]
     */
    fun dismissState() {
        _state.value = PatientState.None
    }
}