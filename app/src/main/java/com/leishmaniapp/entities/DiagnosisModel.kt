package com.leishmaniapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Diagnostic AI model representation
 */
@Serializable
@JvmInline
value class DiagnosisModel(val value: String)

@Entity
data class DiagnosisModelRoom(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val value: String,
)
