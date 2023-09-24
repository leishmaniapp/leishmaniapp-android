package com.leishmaniapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
sealed class DiagnosticElement {
    abstract val name: DiagnosticElementName
    abstract val amount: Int
}

@Entity
data class DiagnosticElementRoom (
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val amount: Int
)