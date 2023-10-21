package com.leishmaniapp.entities

enum class ImageAnalysisStatus {
    NotAnalyzed, /* Image has not been prompted for analysis */
    Deferred, /* No internet connection available, analysis must be done later */
    Analyzing, /* Internet connection is present and image was uploaded */
    Analyzed /* Results were returned from server */
}