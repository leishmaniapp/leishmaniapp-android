package com.leishmaniapp.entities

import android.os.Parcelable
import com.leishmaniapp.entities.serialization.UsernameSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Functional wrapper class around username
 * Inlined: Doesn't generate runtime overhead
 */
@Serializable(with = UsernameSerializer::class)
@Parcelize
data class Username(val value: String): Parcelable