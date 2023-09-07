package com.leishmaniapp.entities

import kotlinx.serialization.Serializable

@Serializable
sealed class DiagnosticElement(
    open val name: String,
    open val amount: Int
)