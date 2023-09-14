package com.leishmaniapp.entities

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val sample: Int,
    val date: LocalDateTime,
    val width: Int,
    val height: Int,
    val processed: Boolean,
    val elements: Set<DiagnosticElement>
)