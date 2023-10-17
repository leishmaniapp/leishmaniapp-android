package com.leishmaniapp.usecases

import androidx.work.Operation
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.Image


interface IImageProcessing {
    suspend fun processImage(diagnosis: Diagnosis, image: Image): Operation
}