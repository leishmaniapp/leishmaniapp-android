package com.leishmaniapp.entities

import kotlinx.serialization.Serializable

@Serializable
data class ImageQueryResponse(
    val processed: Boolean,
    val analysis: Map<DiagnosisModel, List<Coordinates>> = mapOf()
)
