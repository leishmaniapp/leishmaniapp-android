package com.leishmaniapp.entities

import kotlinx.serialization.Serializable

/**
 * Specialist representation
 * Algebraic product type with Username and Password which are inlined
 */
@Serializable
data class Specialist(
    val name: String,
    val username: Username,
    val password: Password
)
