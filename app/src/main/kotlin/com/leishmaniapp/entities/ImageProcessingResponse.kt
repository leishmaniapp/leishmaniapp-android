package com.leishmaniapp.entities

import kotlinx.serialization.Serializable

@Serializable
data class ImageProcessingResponse(
    val sample: Int,
    val analysis: Map<DiagnosticModel, List<Coordinates>>
)