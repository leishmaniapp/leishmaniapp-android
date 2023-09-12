package com.leishmaniapp.entities

import kotlinx.serialization.Serializable

@Serializable
abstract class Disease(
    val id: String,
    val models: Set<DiagnosisModel>,
)
