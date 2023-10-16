package com.leishmaniapp.usecases

import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.Image

interface IImageAnalysisUpload {
    suspend fun uploadImage(image: Image, diagnosis: Diagnosis): Boolean
}