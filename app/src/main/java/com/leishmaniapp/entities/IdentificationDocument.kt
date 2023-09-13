package com.leishmaniapp.entities

import kotlinx.serialization.Serializable
import java.util.Locale

@JvmInline
@Serializable
value class IdentificationDocument(val value: String) {
    override fun toString(): String = value.uppercase(Locale.getDefault())
}