package com.leishmaniapp.entities

import kotlinx.serialization.Serializable

@Serializable
enum class ImageAnalysisStatus {
    NotAnalyzed,
    Analyzing,
    Analyzed
}