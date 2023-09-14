package com.leishmaniapp.entities

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

/**
 * Represents a diagnostic image
 * @immutable Replace by using [Image.copy]
 */
@Serializable
data class Image(
    val sample: Int,
    val date: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val size: Int,
    val processed: Boolean,
    val elements: Set<DiagnosticElement>
)