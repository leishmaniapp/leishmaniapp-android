package com.leishmaniapp.domain.entities

import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Specialization of Diagnostic element for the Specialist
 * Part of [DiagnosticElement] sealed class members
 */
@Parcelize
@Serializable
@SerialName("specialist")
data class SpecialistDiagnosticElement(

    override val id: DiagnosticElementName,
    override val amount: Int

) : DiagnosticElement()