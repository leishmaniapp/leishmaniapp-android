package com.leishmaniapp.domain.entities

/**
 * Representation of the analysis stage for a given image. For internal use only and thus must
 * not be confused with [com.leishmaniapp.cloud.model.AnalysisStage] which is a completely different entity
 */
enum class AnalysisStage {
    /**
     * Image has not been confirmed for analysis
     */
    NotAnalyzed,

    /**
     * Image has been enqueued for analysis, but not confirmed by the backend
     */
    Enqueued,

    /**
     * Image is being analyzed, has been sent to remote server
     */
    Analyzing,

    /**
     * Image has been confirmed for analysis but has not been sent to remote server, no internet access
     */
    Deferred,

    /**
     * Failed to send the image
     * Final error state
     */
    DeliverError,

    /**
     * Image results arrived with error
     * Final error state
     */
    ResultError,

    /**
     * Image results have arrived
     * Final success state
     */
    Analyzed;

    companion object;
}