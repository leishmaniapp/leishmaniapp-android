package com.leishmaniapp.infrastructure.mock

import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.Image
import com.leishmaniapp.persistance.database.ApplicationDatabase
import com.leishmaniapp.usecases.IImageProcessing
import kotlinx.coroutines.flow.Flow

class MockImageProcessingImpl  constructor(val applicationDatabase: ApplicationDatabase): IImageProcessing {
    override suspend fun processImage(diagnosis: Diagnosis, image: Image): Flow<Image> {
        TODO("Not yet implemented")
    }

}