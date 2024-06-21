package com.leishmaniapp.utilities.extensions

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Get the current UTC date as a [LocalDateTime]
 */
fun LocalDateTime.Companion.utcNow(): LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)