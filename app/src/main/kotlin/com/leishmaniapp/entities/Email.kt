package com.leishmaniapp.entities

import android.os.Parcelable
import com.leishmaniapp.usecases.serialization.EmailSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Functional wrapper class around username
 */
@Serializable(with = EmailSerializer::class)
@Parcelize
data class Email(val value: String) : Parcelable