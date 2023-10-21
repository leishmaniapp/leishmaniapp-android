package com.leishmaniapp.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.WorkManager
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.Patient
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.disease.Disease
import com.leishmaniapp.infrastructure.background.ImageProcessingWorker
import com.leishmaniapp.persistance.database.ApplicationDatabase
import com.leishmaniapp.persistance.entities.DiagnosisRoom.Companion.asRoomEntity
import com.leishmaniapp.persistance.entities.ImageRoom
import com.leishmaniapp.persistance.entities.ImageRoom.Companion.asRoomEntity
import com.leishmaniapp.usecases.IDiagnosisSharing
import com.leishmaniapp.usecases.IPictureStandardization
import com.leishmaniapp.usecases.IProcessingRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class DiagnosisViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val applicationDatabase: ApplicationDatabase,
    private val diagnosisShare: IDiagnosisSharing,
    private val pictureStandardization: IPictureStandardization
) : ViewModel() {

    val currentDiagnosis = MutableStateFlow<Diagnosis?>(null)
    val currentImage = MutableStateFlow<Image?>(null)
    val imageFlow: Flow<ImageRoom?>
        get() = runBlocking {
            applicationDatabase.imageDao()
                .imageForDiagnosisFlow(currentDiagnosis.value!!.id, currentImage.value!!.sample)
        }

    init {
        // Read from bundle when activity is destroyed
        savedStateHandle.get<Diagnosis?>("currentDiagnosis")?.let { savedDiagnosis ->
            currentDiagnosis.value = savedDiagnosis
        }
        savedStateHandle.get<Image?>("currentImage")?.let { savedImage ->
            currentImage.value = savedImage
        }
    }


    fun diagnosesForPatient(patient: Patient): List<Diagnosis> {
        val retrievalDiagnosis: MutableList<Diagnosis> = mutableListOf()

        runBlocking {
            val diagnosis = applicationDatabase.diagnosisDao().diagnosesForPatient(patient)

            for (element in diagnosis) {
                val elementSpecialist = applicationDatabase.specialistDao()
                    .specialistByUsername(element.specialistUsername)
                val elementPatient = applicationDatabase.patientDao()
                    .patientById(element.patientIdDocument, element.patientIdType)
                val elementImages = applicationDatabase.imageDao().allImagesForDiagnosis(element.id)

                // Add diagnosis to return value
                retrievalDiagnosis.add(
                    element.asApplicationEntity(elementSpecialist!!,
                        elementPatient!!,
                        elementImages.map { it.asApplicationEntity() })
                )
            }

        }

        return retrievalDiagnosis.toList()
    }

    fun shareCurrentDiagnosis() {
        TODO("Implementation of Diagnosis Sharing Module")
    }

    fun startNewDiagnosis(patient: Patient, specialist: Specialist, disease: Disease) {
        // Create a new diagnosis
        currentDiagnosis.value = Diagnosis(specialist, patient, disease)

        // Store diagnosis in Room
        runBlocking {
            applicationDatabase.diagnosisDao()
                .upsertDiagnosis(currentDiagnosis.value!!.asRoomEntity())
        }
    }

    fun standardizeImage(uri: Uri): Int? {
        if (pictureStandardization.cropPicture(uri)) {
            return pictureStandardization.scalePicture(uri)
        }
        return null
    }

    fun storeImageInDatabase() {
        runBlocking {
            applicationDatabase.imageDao()
                .upsertImage(currentImage.value!!.asRoomEntity(currentDiagnosis.value!!.id))
        }
    }

    fun analyzeImage(context: Context): LiveData<Operation.State> {
        val data = Data.Builder().putString("diagnosis", currentDiagnosis.value!!.id.toString())
            .putInt("sample", currentImage.value!!.sample).build()

        val worker = OneTimeWorkRequestBuilder<ImageProcessingWorker>()
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .setInputData(data)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                duration = Duration.ofSeconds(15)
            ).build()

        return WorkManager.getInstance(context).enqueue(worker).state
    }
}