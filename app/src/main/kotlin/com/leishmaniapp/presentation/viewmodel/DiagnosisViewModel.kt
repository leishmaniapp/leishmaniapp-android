package com.leishmaniapp.presentation.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.entities.AnalysisStage
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.entities.ImageMetadata
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.entities.Patient
import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.domain.protobuf.toProto
import com.leishmaniapp.domain.repository.IDiagnosesRepository
import com.leishmaniapp.domain.repository.IPatientsRepository
import com.leishmaniapp.domain.repository.ISamplesRepository
import com.leishmaniapp.domain.repository.ISpecialistsRepository
import com.leishmaniapp.domain.services.IAuthorizationService
import com.leishmaniapp.domain.services.IDiagnosisSharingService
import com.leishmaniapp.domain.services.IDiagnosisUploadService
import com.leishmaniapp.domain.services.IOngoingDiagnosisService
import com.leishmaniapp.domain.services.IPictureStandardizationService
import com.leishmaniapp.domain.services.IQueuingService
import com.leishmaniapp.presentation.viewmodel.state.DiagnosisState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
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
    authorizationService: IAuthorizationService,
    private val ongoingDiagnosisService: IOngoingDiagnosisService,
    private val queuingService: IQueuingService,
    private val pictureStandardizationService: IPictureStandardizationService,
    private val diagnosisSharingService: IDiagnosisSharingService,
    private val diagnosisUploadService: IDiagnosisUploadService,

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

    /**
     * Handle specialist credentials for getting current ongoing diagnosis
     */
    private val credentials =
        authorizationService.credentials
            .onEach { c -> Log.d(TAG, "Credentials for ongoing diagnosis (${c?.email})") }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)


    /* -- Disease Selection -- */

    private val _disease: MutableLiveData<Disease?> =
        savedStateHandle.getLiveData("disease", null)

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

    // Get the ongoing diagnosis
    private val ongoingDiagnosis =
        credentials
            .flatMapLatest { c ->
                if (c != null) {
                    ongoingDiagnosisService.getOngoingDiagnosis(c.email)
                } else {
                    flowOf(null)
                }
            }
            .flowOn(Dispatchers.IO)
            .onEach { d -> Log.d(TAG, d?.let { "Ongoing (${d})" } ?: "No ongoing diagnosis") }
            .catch { e -> Log.e(TAG, "Failed to gather OngoingDiagnosis", e) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /**
     * Current [Diagnosis] being constructed
     */
    val diagnosis: StateFlow<Diagnosis?> =
        ongoingDiagnosis
            .flatMapLatest { uuid ->
                if (uuid != null) {
                    diagnosesRepository.getDiagnosis(uuid)
                } else {
                    flowOf(null)
                }
            }
            .flowOn(Dispatchers.IO)
            .onEach { d -> Log.i(TAG, d?.let { "Loaded (${d.id})" } ?: "No diagnosis found") }
            .catch { e -> Log.e(TAG, "Exception thrown during Diagnosis StateFlow collection", e) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /**
     * [Specialist] associated with the diagnosis for internal use
     */
    private val specialist: StateFlow<Specialist?> = diagnosis
        .flatMapMerge { diagnosis ->
            if (diagnosis != null) {
                specialistsRepository.specialistByEmail(diagnosis.specialist.email)
            } else {
                flowOf(null)
            }
        }
        .flowOn(Dispatchers.IO)
        .catch { e -> Log.e(TAG, "Exception thrown during Specialist StateFlow collection", e) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

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
                    withContext(Dispatchers.IO) {
                        diagnosesRepository.upsertDiagnosis(diagnosis)
                    }

                    // Set as ongoing diagnosis
                    ongoingDiagnosisService.setOngoingDiagnosis(specialist.email, diagnosis.id)
                    Log.i(
                        TAG,
                        "Created new diagnosis (${specialist.email}) with specialist (${specialist.email})"
                    )
                }
            }
        }
    }

    /**
     * Set the current ongoing diagnosis
     */
    fun setCurrentDiagnosis(diagnosis: Diagnosis) = viewModelScope.launch(Dispatchers.IO) {
        ongoingDiagnosisService.setOngoingDiagnosis(
            diagnosis.specialist.email,
            diagnosis.id
        )
    }

    /**
     * Set the current [Diagnosis]
     */
    fun setBackgroundDiagnosis() {
        viewModelScope.launch {
            diagnosis.value?.let { d ->
                diagnosesRepository.upsertDiagnosis(d.copy(background = true))
            }
        }
    }

    /**
     * Finalize the current [Diagnosis] and compute the results
     */
    fun finalizeDiagnosis(remarks: String?, specialistResult: Boolean) {
        diagnosis.value?.let { d ->
            viewModelScope.launch {
                // Finalize the diagnosis
                // Compute results in Default dispatcher
                withContext(Dispatchers.Default) {
                    d.copy(
                        remarks = remarks,
                        finalized = true
                    ).withResults(specialistResult)
                }.let { finalizedDiagnosis ->

                    // Push results with IO dispatcher
                    withContext(Dispatchers.IO) {
                        diagnosesRepository.upsertDiagnosis(
                            finalizedDiagnosis
                        )
                    }

                    // Upload the diagnosis
                    diagnosisUploadService.uploadAsync(
                        finalizedDiagnosis.toProto()
                    )
                }
            }
        }
    }

    fun shareDiagnosis(context: Context) = diagnosis.value?.let { d ->
        viewModelScope.launch {
            // Create the diagnosis file
            val file = withContext(Dispatchers.IO) { diagnosisSharingService.share(d) }

            // Share the file
            val uri = FileProvider.getUriForFile(context, "com.leishmaniapp", file)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = diagnosisSharingService.mime
                putExtra(Intent.EXTRA_STREAM, uri)
            }

            //
            context.startActivity(Intent.createChooser(intent, "Share via"))
        }
    }

    /**
     * [ImageMetadata] for identifying the [ImageSample]
     */
    private val currentImageMetadata: MutableLiveData<ImageMetadata?> =
        savedStateHandle.getLiveData("currentImageSample", null)

    /**
     * Actual [ImageSample] flow from database based on the [currentImageMetadata]
     */
    val currentImageSample: StateFlow<ImageSample?> = currentImageMetadata.asFlow()
        .flatMapLatest { metadata ->
            if (metadata != null) {
                samplesRespository.getSampleForMetadata(metadata)
            } else {
                flowOf(null)
            }
        }
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)
        .onEach { s -> Log.d(TAG, "CurrentImageSample=${s.toString()}") }
        .catch { e ->
            Log.e(
                TAG,
                "Exception thrown during ImageSample StateFlow collection",
                e
            )
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /**
     * Create a new [ImageSample]
     */
    fun setNewImageSample(uri: Uri) {
        // Set the image sample given the current diagnosis
        viewModelScope.launch {
            diagnosis.value?.let { diagnosis ->
                ImageSample(diagnosis = diagnosis, file = uri).let { sample ->
                    // Store the sample in database
                    withContext(Dispatchers.IO) {
                        samplesRespository.upsertSample(sample)
                        Log.d(TAG, "Sample stored: (${sample})")
                    }
                    // Set the metadata value
                    currentImageMetadata.value = sample.metadata
                }
            }
        }
    }

    /**
     * Set the current [ImageSample] using an already existing one
     */
    fun setCurrentImageSample(sample: ImageSample) {
        currentImageMetadata.value = sample.metadata
    }

    /**
     * Update the current [ImageSample]
     */
    fun updateImageSample(imageSample: ImageSample) {
        viewModelScope.launch(Dispatchers.IO) {
            samplesRespository.upsertSample(imageSample)
            Log.i(TAG, "Pushing replacement for CurrentImageSample (${imageSample.metadata})")
        }
    }

    /**
     * Update the current [Diagnosis] appending the latest [ImageSample]
     */
    fun discardCurrentImageSample() {
        viewModelScope.launch(Dispatchers.IO) {
            samplesRespository.deleteSample(currentImageSample.value!!)
        }
    }

    /**
     * Save the current image sample, gets discarted if not analyzed
     */
    fun saveImageSample(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            currentImageSample.value?.let { image ->

                // Do not save the sample if it is not analyzed
                if (image.stage == AnalysisStage.NotAnalyzed) {
                    return@launch
                }

                try {
                    // Create the file
                    val file = File(
                        context.cacheDir,
                        "diagnoses/${image.metadata.diagnosis}/${image.metadata.sample}${pictureStandardizationService.fileExtension}"
                    )

                    // Create directories and files
                    file.parentFile?.mkdirs()
                    file.createNewFile()

                    // Store the file
                    pictureStandardizationService.store(file, image.bitmap!!).getOrThrow()

                    // Update the sample
                    samplesRespository.upsertSample(image.copy(file = file.toUri()))

                } catch (e: Exception) {
                    Log.e(TAG, "Failed to store ImageSample image in cache", e)
                }
            }
        }
    }

    /**
     * Push the current image sample to analysis
     */
    fun startSampleAnalysis() {
        if (currentImageSample.value != null && specialist.value != null) {
            viewModelScope.launch {
                queuingService.enqueue(
                    currentImageSample.value!!,
                    specialist.value!!.email,
                    pictureStandardizationService.mimeType
                )
            }
        }
    }

    /**
     * Restart the current [DiagnosisState]
     */
    override fun dismiss() {
        // Remove ongoing diagnosis
        viewModelScope.launch {
            credentials.value?.email?.let { email ->
                ongoingDiagnosisService.removeOngoingDiagnosis(email)
            }
        }
        // Remove image metadata
        currentImageMetadata.value = null
    }

    /**
     * Discard the [Diagnosis] with all the associated [ImageSample] and then [dismiss]
     */
    fun discard() {
        diagnosis.value?.let { diagnosis ->
            if (!diagnosis.finalized)
                viewModelScope.launch {
                    // Delete images
                    diagnosis.deleteImageFiles()
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