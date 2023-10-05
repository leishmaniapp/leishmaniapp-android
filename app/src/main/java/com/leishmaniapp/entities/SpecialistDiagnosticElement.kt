package com.leishmaniapp.entities

import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Specialization of Diagnostic element for the Specialist
 * Part of [DiagnosticElement] sealed class members
 */
@Serializable
@SerialName("specialist")
@Parcelize
data class SpecialistDiagnosticElement(
    override val name: DiagnosticElementName,
    override val amount: Int
) : DiagnosticElement()