package com.leishmaniapp.entities

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class IdentificationDocument(
    val value: String
)