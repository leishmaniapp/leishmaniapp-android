package com.leishmaniapp.utilities.time

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * Transform into Unix time
 */
fun LocalDateTime.toUnixTime(): Long =
    toInstant(TimeZone.UTC).toEpochMilliseconds()

/**
 * Transform Unix time into a [LocalDateTime]
 */
fun Long.fromUnixToLocalDateTime(): LocalDateTime =
    Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.UTC)