package com.leishmaniapp.entities

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val date: LocalDateTime,
    val width: Int,
    val height: Int,
    val processed: Boolean,
    val sample: Int,
    val diagnosticElements: MutableList<DiagnosticElement>
)