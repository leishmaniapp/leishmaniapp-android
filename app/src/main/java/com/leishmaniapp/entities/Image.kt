package com.leishmaniapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Represents a diagnostic image
 * @immutable Replace by using [Image.copy]
 */

@Entity
data class ImageRoom(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val sample: Int,
    val date: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val size: Int,
    val processed: Boolean,
    val elements: String
)

@Serializable
data class Image(
    val sample: Int,
    val date: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val size: Int,
    val processed: Boolean,
    val elements: Set<DiagnosticElement>
)