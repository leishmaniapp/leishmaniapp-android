package com.leishmaniapp.infrastructure.analysis

import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.services.IQueuingService

/**
 * [IQueuingService] implementation using WorkManager
 */
class WorkQueuingServiceImpl: IQueuingService {
    override fun enqueue(sample: ImageSample): Result<Unit> {
        TODO("Not yet implemented")
    }
}