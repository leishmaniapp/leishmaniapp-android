package com.leishmaniapp.entities

import kotlinx.serialization.Serializable

@Serializable
sealed class DiagnosticElement {
    abstract val name: DiagnosticElementName
    abstract val amount: Int
}