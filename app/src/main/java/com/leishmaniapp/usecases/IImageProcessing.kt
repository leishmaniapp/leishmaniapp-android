package com.leishmaniapp.usecases

import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.Image
import com.leishmaniapp.persistance.database.ApplicationDatabase
import kotlinx.coroutines.flow.Flow

interface IImageProcessing {
    /**
     * Call [WorkerManager] to process image
     * The Image must be altered directly on the [ApplicationDatabase]
     */
    suspend fun processImage(diagnosis: Diagnosis, image: Image): Flow<Image>

}