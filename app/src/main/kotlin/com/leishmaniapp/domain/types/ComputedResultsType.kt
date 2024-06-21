package com.leishmaniapp.domain.types

import com.leishmaniapp.domain.entities.DiagnosticElement
import com.leishmaniapp.domain.entities.ModelDiagnosticElement
import com.leishmaniapp.domain.entities.SpecialistDiagnosticElement
import com.leishmaniapp.domain.entities.DiagnosticElementName
import kotlin.reflect.KClass

/**
 * Alias for a map with [DiagnosticElementName] as key and a map containing both
 * [ModelDiagnosticElement] and [SpecialistDiagnosticElement] and total amount as value
 */
typealias ComputedResultsType = Map<DiagnosticElementName, Map<KClass<out DiagnosticElement>, Int>>