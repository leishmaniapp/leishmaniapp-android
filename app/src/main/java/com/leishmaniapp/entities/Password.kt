package com.leishmaniapp.entities

import androidx.room.TypeConverter

/**
 * Functional wrapper class around password
 * Inlined: Doesn't generate runtime overhead
 *
 * This class is NOT serializable
 */
data class Password(val value: String)