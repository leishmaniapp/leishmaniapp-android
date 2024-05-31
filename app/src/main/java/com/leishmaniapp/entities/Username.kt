package com.leishmaniapp.entities

import android.os.Parcelable
import com.leishmaniapp.usecases.serialization.UsernameSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Functional wrapper class around username
 */
@Serializable(with = UsernameSerializer::class)
@Parcelize
data class Username(val value: String) : Parcelable