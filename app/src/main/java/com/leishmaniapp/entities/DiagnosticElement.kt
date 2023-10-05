package com.leishmaniapp.entities

import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
sealed class DiagnosticElement: Parcelable {
    abstract val name: DiagnosticElementName
    abstract val amount: Int
}