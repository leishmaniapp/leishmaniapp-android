package com.leishmaniapp.entities

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
sealed class DiagnosticElement(
    open val name: String,
    open val amount: Int
)