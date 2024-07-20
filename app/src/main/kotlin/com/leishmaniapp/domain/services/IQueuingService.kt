package com.leishmaniapp.domain.services

import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.types.Email

/**
 * Queue [ImageSample] for further (local or remote) analysis
 */
interface IQueuingService {

    /**
     * Listen for analysis results
     */
    suspend fun startSync()

    /**
     * Stop listening for results
     */
    suspend fun cancelSync()

    /**
     * Enqueue an [ImageSample] for analysis
     * @param mime MIME type associated to the sample from [IPictureStandardizationService]
     */
    suspend fun enqueue(sample: ImageSample, specialist: Email, mime: String)
}