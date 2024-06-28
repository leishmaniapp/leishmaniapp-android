package com.leishmaniapp.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.entities.Patient
import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.domain.repository.IDiagnosesRepository
import com.leishmaniapp.domain.repository.IPatientsRepository
import com.leishmaniapp.domain.repository.ISamplesRepository
import com.leishmaniapp.domain.repository.ISpecialistsRepository
import com.leishmaniapp.domain.services.IPictureStandardization
import com.leishmaniapp.infrastructure.picture.ApplicationPictureStandardizationImpl
import com.leishmaniapp.presentation.viewmodel.state.DiagnosisState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

/**
 * Handle the complete [Diagnosis] control flow
 */
@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class DiagnosisViewModel @Inject constructor(

    /**
     * Keep state accross configuration changes
     */
    savedStateHandle: SavedStateHandle,

    // Services
    private val pictureStandardizationService: IPictureStandardization,

    // Repositories
    private val diagnosesRepository: IDiagnosesRepository,
    private val samplesRespository: ISamplesRepository,
    private val specialistsRepository: ISpecialistsRepository,
    private val patientsRepository: IPatientsRepository,

    ) : ViewModel(), DismissableState {

    companion object {
        /**
         * TAG to use within [Log]
         */
        val TAG: String = DiagnosisViewModel::class.simpleName!!
    }


    /* -- Disease Selection -- */

    private val _disease: MutableLiveData<Disease?> = savedStateHandle.getLiveData("disease", null)

    /**
     * Selected disease by the specialist
     */
    val disease: LiveData<Disease?> = _disease

    /**
     * Select a new disease for diagnosis
     */
    fun setDisease(disease: Disease) {
        Log.i(TAG, "Disease changed to (${disease.id})")
        _disease.value = disease
    }

    /**
     * Delete the already selected [Disease]
     */
    fun dismissDisease() {
        Log.i(TAG, "Disease deleted, using null")
        _disease.value = null
    }

    /* -- Diagnosis State -- */

    private val _state: MutableLiveData<DiagnosisState> =
        savedStateHandle.getLiveData("state", DiagnosisState.None)

    /**
     * Current diagnosis state
     */
    val state: LiveData<DiagnosisState> = _state

    /**
     * Current [Diagnosis] being constructed
     */
    val diagnosis: StateFlow<Diagnosis?> = state.asFlow().flatMapMerge { state ->
        when (state) {
            is DiagnosisState.OnDiagnosis -> diagnosesRepository.diagnosisForId(state.id)
            is DiagnosisState.None -> flowOf(null)
        }
    }.flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /**
     * [Specialist] associated with the diagnosis for internal use
     */
    private val specialist: StateFlow<Specialist?> = diagnosis.flatMapMerge { diagnosis ->
        if (diagnosis != null) {
            specialistsRepository.specialistByEmail(diagnosis.specialist.email)
        } else {
            flowOf(null)
        }
    }.flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /**
     * [Patient] associated with the diagnosis for internal use
     */
    private val patient: StateFlow<Patient?> = diagnosis.flatMapMerge { diagnosis ->
        if (diagnosis != null) {
            patientsRepository.patientById(diagnosis.patient.id, diagnosis.patient.documentType)
        } else {
            flowOf(null)
        }
    }.flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /**
     * Start a new [Diagnosis] given a [Specialist] and a [Patient]
     */
    fun startDiagnosis(specialist: Specialist.Record, patient: Patient) {
        disease.value?.let { disease ->
            // Create a new diagnosis with the given values
            Diagnosis(
                specialist = specialist,
                patient = patient,
                disease = disease,
            ).let { diagnosis ->
                viewModelScope.launch {
                    // Store in database
                    withContext(Dispatchers.IO) { diagnosesRepository.upsertDiagnosis(diagnosis) }
                    // Set the new state
                    _state.value = DiagnosisState.OnDiagnosis(diagnosis.id)
                }
            }
        }
    }

    private val _currentImageSample: MutableLiveData<ImageSample?> =
        savedStateHandle.getLiveData("currentImageSample", null)

    /**
     * Current [ImageSample] being modified
     */
    val currentImageSample: LiveData<ImageSample?> = _currentImageSample

    /**
     * Create a new [ImageSample]
     */
    fun setCurrentImageSample(uri: Uri) {
        // Set the image sample given the current diagnosis
        diagnosis.value?.let { diagnosis ->
            _currentImageSample.value = ImageSample(diagnosis = diagnosis, file = uri)
        }
    }

    /**
     * Change the current [ImageSample]
     */
    fun updateImageSample(imageSample: ImageSample){
        _currentImageSample.value = imageSample
    }

    /**
     * Store the current [ImageSample] in database and reset the [currentImageSample]
     */
    fun saveSampleAndContinue(context: Context) {
        currentImageSample.value?.let { sample ->
            viewModelScope.launch {
                pictureStandardizationService.store(
                    File(
                        context.filesDir,
                        sample.metadata.diagnosis.toString() + File.separator + sample.metadata.sample
                    ), sample.bitmap!!
                )
                withContext(Dispatchers.IO) { samplesRespository.upsertImage(sample) }
                _currentImageSample.value = null
            }
        }
    }

    /**
     * Restart the current [DiagnosisState]
     */
    override fun dismiss() {
        // Set the state to none
        _state.value = DiagnosisState.None
        _currentImageSample.value = null
    }

    /**
     * Discard the [Diagnosis] with all the associated [ImageSample] and then [dismiss]
     */
    fun discard() {
        diagnosis.value?.let { diagnosis ->
            viewModelScope.launch {
                // Delete the current diagnosis
                withContext(Dispatchers.IO) {
                    diagnosesRepository.deleteDiagnosis(diagnosis)
                }

                // Dismiss state
                dismiss()
            }
        }
    }
}