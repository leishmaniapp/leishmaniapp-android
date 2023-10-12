package com.leishmaniapp.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.Image
import com.leishmaniapp.entities.Patient
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.disease.Disease
import com.leishmaniapp.persistance.database.ApplicationDatabase
import com.leishmaniapp.usecases.IPictureStandardization
import com.leishmaniapp.usecases.types.IDiagnosisSharing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DiagnosisViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val applicationDatabase: ApplicationDatabase,
    private val diagnosisShare: IDiagnosisSharing,
    private val pictureStandardization: IPictureStandardization,
) : ViewModel() {

    val currentDiagnosis = MutableStateFlow<Diagnosis?>(null)
    val currentImage = MutableStateFlow<Image?>(null)

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
    }

    fun standardizeImage(uri: Uri): Int? {
        if (pictureStandardization.cropPicture(uri)) {
            return pictureStandardization.scalePicture(uri)
        }
        return null
    }
}