package com.leishmaniapp.entities

import kotlinx.serialization.Serializable

/**
 * Functional wrapper class around username
 * Inlined: Doesn't generate runtime overhead
 */
@JvmInline
@Serializable
value class Username(val value: String)