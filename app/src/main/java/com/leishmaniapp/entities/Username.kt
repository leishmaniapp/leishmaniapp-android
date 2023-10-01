package com.leishmaniapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Functional wrapper class around username
 * Inlined: Doesn't generate runtime overhead
 */
@JvmInline
@Serializable
value class Username(val value: String)