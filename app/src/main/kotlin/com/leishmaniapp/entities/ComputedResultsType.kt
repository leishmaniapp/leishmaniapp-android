package com.leishmaniapp.entities

import kotlin.reflect.KClass

typealias ComputedResultsType = Map<DiagnosticElementName, Map<KClass<out DiagnosticElement>, Int>>