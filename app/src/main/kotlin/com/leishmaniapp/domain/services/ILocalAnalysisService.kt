package com.leishmaniapp.domain.services

import com.leishmaniapp.domain.entities.ImageSample

/**
 * Request local data analysis
 */
interface ILocalAnalysisService {
    /**
     * Try to analyze a sample, return true if analysis is possible, false if LAM module is not
     * installed. Returns an exception if some other problem occurs
     */
    suspend fun tryAnalyze(sample: ImageSample): Result<Boolean>
}