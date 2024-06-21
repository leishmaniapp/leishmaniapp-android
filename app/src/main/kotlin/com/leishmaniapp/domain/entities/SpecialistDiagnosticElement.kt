package com.leishmaniapp.domain.entities

import kotlinx.parcelize.Parcelize

/**
 * Specialization of Diagnostic element for the Specialist
 * Part of [DiagnosticElement] sealed class members
 */
@Parcelize
data class SpecialistDiagnosticElement(

    override val id: DiagnosticElementName,
    override val amount: Int

) : DiagnosticElement()