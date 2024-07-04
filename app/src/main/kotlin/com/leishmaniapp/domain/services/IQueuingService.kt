package com.leishmaniapp.domain.services

import com.leishmaniapp.domain.entities.ImageSample

/**
 * Queue [ImageSample] for further (local or remote) analysis
 */
interface IQueuingService {

    /**
     * Enqueue an [ImageSample] for analysis
     */
    fun enqueue(sample: ImageSample): Result<Unit>
}