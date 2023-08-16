package com.leishmaniapp.entities

/**
 * Specialization of Diagnostic element for the Specialist
 * Part of [DiagnosticElement] sealed class members
 */
data class SpecialistDiagnosticElement(
    override val name: String,
    override val amount: Int
) : DiagnosticElement(name, amount);
