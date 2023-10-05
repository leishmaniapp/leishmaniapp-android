package com.leishmaniapp.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class IdentificationDocument(val value: String) : Parcelable