package com.leishmaniapp.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Diagnostic AI model representation
 */
/**
 * Diagnostic AI model representation
 */
@Serializable
@JvmInline
@Parcelize
value class DiagnosisModel(val value: String) : Parcelable

@Entity
data class DiagnosisModelRoom(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val value: String,
)