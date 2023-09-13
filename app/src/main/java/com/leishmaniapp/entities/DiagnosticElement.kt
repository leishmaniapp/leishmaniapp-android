package com.leishmaniapp.entities

import kotlinx.serialization.Serializable

@Serializable
sealed class DiagnosticElement(
    open val name: DiagnosticElementName,
    open val amount: Int
)