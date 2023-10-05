package com.leishmaniapp.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Functional wrapper class around password
 * Inlined: Doesn't generate runtime overhead
 *
 * This class is NOT serializable
 */
@Parcelize
data class Password(val value: String): Parcelable