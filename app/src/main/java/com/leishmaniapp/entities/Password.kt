package com.leishmaniapp.entities

import kotlinx.serialization.Serializable

/**
 * Functional wrapper class around password
 * Inlined: Doesn't generate runtime overhead
 */
@JvmInline
@Serializable
value class Password(val value: String)