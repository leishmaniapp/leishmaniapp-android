package com.leishmaniapp.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
sealed class DiagnosticElement: Parcelable {
    abstract val name: DiagnosticElementName
    abstract val amount: Int
}