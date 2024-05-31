package com.leishmaniapp.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Functional wrapper class around password
 *
 * This class is NOT serializable, sensitive data
 */
@Parcelize
data class Password(val value: String) : Parcelable