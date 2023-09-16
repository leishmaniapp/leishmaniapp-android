package com.leishmaniapp.entities

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Specialist representation
 * Algebraic product type with Username and Password which are inlined
 */
@Serializable
data class Specialist(
    val name: String,
    val username: Username,
    @Transient val password: Password? = null
)
